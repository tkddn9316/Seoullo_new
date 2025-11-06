package com.app.data.repository

import android.net.Uri
import com.app.data.mapper.toComment
import com.app.data.mapper.toDto
import com.app.data.mapper.toPost
import com.app.data.mapper.toReply
import com.app.data.utils.Util.DELETED_AUTHOR_ID
import com.app.domain.model.DeletionResult
import com.app.domain.model.User
import com.app.domain.model.community.Comment
import com.app.domain.model.community.Post
import com.app.domain.repository.BoardRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storageMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

class BoardRepositoryImpl @Inject constructor(
    @Named("boardRef") private val boardRef: CollectionReference,
    private val imageOptRepo: ImageOptimizationRepository,
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val functions: FirebaseFunctions
) : BoardRepository {

    // 게시글 올리기
    override fun addPost(
        user: User,
        title: String,
        content: String,
        images: List<Uri>
    ): Flow<String> = flow {
        val postRef = boardRef.document()
        val postId = postRef.id
        val urls = uploadPostImages(postId, images)

        val post = Post(
            id = postId,
            title = title,
            content = content,
            authorId = auth.currentUser?.uid ?: user.tokenId,
            authorName = user.name,
            authorPhotoUrl = user.photoUrl,
            createdAt = System.currentTimeMillis(),
            likeCount = 0,
            commentCount = 0,
            imageUrls = urls
        )
        postRef.set(post.toDto()).await()

        emit(postId)
    }

    /**
     * @param postId           수정할 게시글 ID
     * @param title            최종 제목
     * @param content          최종 내용
     * @param keptRemoteUrls   기존 원격 이미지 중 유지할 것들(URL)
     * @param newLocalUris     이번에 새로 추가된 로컬 이미지 URI 목록
     * @param deleteRemoved    true면 기존에서 빠진 원격 이미지는 Storage에서 삭제 시도
     */
    override suspend fun updatePost(
        user: User,
        postId: String,
        title: String,
        content: String,
        keptRemoteUrls: List<String>,
        newLocalUris: List<Uri>,
        deleteRemoved: Boolean
    ): Flow<String> = flow {
        val rawUid = auth.currentUser?.uid
        val authId = if (!rawUid.isNullOrBlank()) rawUid else user.tokenId
        val postRef = boardRef.document(postId)

        val snap = postRef.get().await()
        if (!snap.exists()) throw IllegalArgumentException("Post not found")

        val authorId = snap.getString("authorId") ?: ""
        if (authorId != authId) throw SecurityException("Only author can edit")

        val oldUrls: List<String> = (snap.get("imageUrls") as? List<*>)?.filterIsInstance<String>().orEmpty()
        val uploadedUrls = uploadPostImages(postId, newLocalUris)
        val finalUrls = (keptRemoteUrls + uploadedUrls).distinct()

        val updates = mapOf(
            "title" to title,
            "content" to content,
            "imageUrls" to finalUrls
        )
        // 업데이트
        postRef.update(updates).await()

        // 이미지 삭제한거 있으면 storage 반영
        if (deleteRemoved) {
            val removed = oldUrls.toSet() - keptRemoteUrls.toSet()
            coroutineScope {
                removed.map { url ->
                    async(Dispatchers.IO) {
                        runCatching { storage.getReferenceFromUrl(url).delete().await() }
                    }
                }.awaitAll()
            }
        }

        emit(postId)
    }

    override suspend fun deletePost(postId: String): Result<DeletionResult> = runCatching {
        val result = functions
            .getHttpsCallable("deletePost")
            .call(mapOf("postId" to postId))
            .await()

        @Suppress("UNCHECKED_CAST")
        val data = result.data as? Map<String, Any?> ?: emptyMap()
        val docs = (data["deletedDocs"] as? Number)?.toInt() ?: 0
        val files = (data["deletedFiles"] as? Number)?.toInt() ?: 0
        DeletionResult(docs, files)
    }

    // 게시글 목록
    override fun observePosts(): Flow<List<Post>> =
        boardRef
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshotsFlow()
            .map { qs -> qs.documents.mapNotNull { it.toPost() } }

    // 게시글 가져오기(단일)
    override fun getPost(postId: String): Flow<Post?> =
        boardRef
            .document(postId)
            .snapshotsFlow()
            .map { it?.toPost() }

    // 댓글 달기
    override suspend fun addComment(postId: String, user: User, text: String): Flow<Unit> = flow {
        val postRef = boardRef.document(postId)
        val commentRef = postRef.collection("comments").document()
        val comment = Comment(
            id = commentRef.id,
            text = text,
            authorId = auth.currentUser?.uid ?: user.tokenId,
            authorName = user.name,
            authorPhotoUrl = user.photoUrl,
            createdAt = System.currentTimeMillis()
        )

        db.runBatch { batch ->
            batch.set(commentRef, comment.toDto())
            batch.update(postRef, "commentCount", FieldValue.increment(1))
        }.await()

        emit(Unit)
    }

    // 댓글 삭제
    override suspend fun deleteComment(postId: String, commentId: String): Flow<Unit> = flow {
        val postRef = boardRef.document(postId)
        val commentRef = postRef.collection("comments").document(commentId)
        val replySize = commentRef.collection("reply").get().await().size()

        db.runTransaction { tx ->
            val snap = tx.get(commentRef)
            if (!snap.exists()) return@runTransaction

            tx.update(postRef, "commentCount", FieldValue.increment(-1))
            if (replySize > 0) {
                // 답글이 존재할 경우
                // 이미 소프트 삭제 되었는지 체크
                val isDeleted = snap.getBoolean("isDeleted") ?: false
                val authorId = snap.getString("authorId") ?: ""
                if (isDeleted || authorId == DELETED_AUTHOR_ID) return@runTransaction

                // 2) 댓글 소프트 삭제 (답글은 건드리지 않음)
                val updates = mutableMapOf<String, Any>(
                    "isDeleted" to true,
                    "authorName" to "",
                    "authorPhotoUrl" to "",
                    "authorId" to DELETED_AUTHOR_ID,
                    "text" to ""
                )

                tx.update(commentRef, updates as Map<String, Any>)
            } else {
                tx.delete(commentRef)
            }
        }
    }

    // 답글 달기
    override suspend fun addReply(
        postId: String,
        commentId: String,
        user: User,
        text: String
    ): Flow<Unit> = flow {
        val postRef = boardRef.document(postId)
        val replyRef = postRef
            .collection("comments")
            .document(commentId)
            .collection("reply")
            .document()
        val reply = Comment.Reply(
            id = replyRef.id,
            text = text,
            authorId = auth.currentUser?.uid ?: user.tokenId,
            authorName = user.name,
            authorPhotoUrl = user.photoUrl,
            createdAt = System.currentTimeMillis()
        )

        db.runBatch { batch ->
            batch.set(replyRef, reply.toDto())
            batch.update(postRef, "commentCount", FieldValue.increment(1))
        }.await()

        emit(Unit)
    }

    // 답글 삭제
    override suspend fun deleteReply(
        postId: String,
        commentId: String,
        replyId: String
    ): Flow<Unit> = flow {
        val postRef = boardRef.document(postId)
        val replyRef = postRef
            .collection("comments")
            .document(commentId)
            .collection("reply")
            .document(replyId)

        db.runBatch { batch ->
            batch.delete(replyRef)
            batch.update(postRef, "commentCount", FieldValue.increment(-1))
        }.await()

        emit(Unit)
    }

    // 댓글 목록
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeCommentsWithReplies(postId: String): Flow<List<Comment>> =
        observeComments(postId = postId).flatMapLatest { comments ->
            if (comments.isEmpty()) return@flatMapLatest flowOf(emptyList())

            val replyFlows: List<Flow<List<Comment.Reply>>> =
                comments.map { comment -> observeReplies(postId = postId, commentId = comment.id) }

            combine(replyFlows) { repliesArray ->
                comments.mapIndexed { index, comment ->
                    comment.copy(replyList = repliesArray.getOrNull(index).orEmpty())
                }
            }
        }

    private fun observeComments(postId: String): Flow<List<Comment>> =
        boardRef.document(postId)
            .collection("comments")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .snapshotsFlow()
            .map { qs -> qs.documents.mapNotNull { it.toComment() } }

    private fun observeReplies(
        postId: String,
        commentId: String
    ): Flow<List<Comment.Reply>> =
        boardRef.document(postId)
            .collection("comments")
            .document(commentId)
            .collection("reply")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .snapshotsFlow()
            .map { qs ->
                qs.documents.mapNotNull { it.toReply() }
            }

    // 좋아요 여부
    override suspend fun hasLiked(postId: String, user: User): Boolean {
        val rawUid = auth.currentUser?.uid
        val authId = if (!rawUid.isNullOrBlank()) rawUid else user.tokenId

        if (authId.isBlank()) return false

        val ref = boardRef.document(postId)
            .collection("like")
            .document(authId)

        return ref.get().await().exists()
    }

    // 좋아요 올리기
    override suspend fun like(postId: String, user: User): Flow<Unit> = flow {
        val authId = auth.currentUser?.uid ?: user.tokenId
        val data = mapOf(
            "authorId" to authId,
            "likedAt" to System.currentTimeMillis()
        )

        val postRef = boardRef.document(postId)
        db.runTransaction { tx ->
            val postSnapshot = tx.get(postRef)
            val likeRef = postRef.collection("like").document(authId)
            val likeSnapshot = tx.get(likeRef)

            // 이미 좋아요 눌렀으면 아무 작업도 하지 않음
            if (likeSnapshot.exists()) return@runTransaction

            val cur = postSnapshot.getLong("likeCount") ?: 0L
            tx.update(postRef, "likeCount", cur + 1)
            tx.set(likeRef, data)
        }.await()

        emit(Unit)
    }

    // 좋아요 삭제
    override suspend fun unlike(postId: String, user: User): Flow<Unit> = flow {
        val authId = auth.currentUser?.uid ?: user.tokenId
        val postRef = boardRef.document(postId)

        db.runTransaction { tx ->
            val postSnapshot = tx.get(postRef)
            val likeRef = postRef.collection("like").document(authId)
            val likeSnapshot = tx.get(likeRef)

            // 좋아요가 없는데 취소하려 하면 아무 작업도 하지 않음
            if (!likeSnapshot.exists()) return@runTransaction

            val cur = postSnapshot.getLong("likeCount") ?: 0L
            tx.update(postRef, "likeCount", (cur - 1).coerceAtLeast(0))
            tx.delete(likeRef)
        }.await()

        emit(Unit)
    }

    // 이미지 업로드
    private suspend fun uploadPostImages(postId: String, uris: List<Uri>): List<String> =
        coroutineScope {
            val baseRef = storage.reference.child("posts/$postId")
            uris.map { uri ->
                async(Dispatchers.IO) {
                    // 1. URI → 최적화된 EncodedImage (리사이즈 + 압축)
                    val encoded = imageOptRepo.optimizeUri(uri)

                    // 2. Firebase 업로드 경로 및 메타데이터
                    val fileName =
                        "${System.currentTimeMillis()}_${UUID.randomUUID()}.${encoded.extension}"
                    val fileRef = baseRef.child(fileName)
                    val meta = storageMetadata { contentType = encoded.contentType }

                    // 3. 업로드
                    fileRef.putBytes(encoded.bytes, meta).await()

                    // 4. 다운로드 URL
                    fileRef.downloadUrl.await().toString()
                }
            }.awaitAll()
        }

    // 실시간 쿼리 스냅샷
    private fun Query.snapshotsFlow(): Flow<QuerySnapshot> = callbackFlow {
        val reg = addSnapshotListener { qs, e ->
            if (e != null) {
                close(e); return@addSnapshotListener
            }
            if (qs != null) trySend(qs)
        }
        awaitClose { reg.remove() }
    }

    private fun DocumentReference.snapshotsFlow(): Flow<DocumentSnapshot?> = callbackFlow {
        val listener = addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
            } else {
                trySend(snapshot)
            }
        }
        awaitClose { listener.remove() }
    }
}