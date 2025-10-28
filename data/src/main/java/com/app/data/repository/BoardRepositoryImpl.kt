package com.app.data.repository

import android.net.Uri
import com.app.data.mapper.toComment
import com.app.data.mapper.toDto
import com.app.data.mapper.toPost
import com.app.domain.model.User
import com.app.domain.model.community.Comment
import com.app.domain.model.community.Post
import com.app.domain.repository.BoardRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storageMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
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
    private val storage: FirebaseStorage
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

    // 게시글 목록
    override fun observePosts(): Flow<List<Post>> =
        boardRef
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshotsFlow()
            .map { qs -> qs.documents.mapNotNull { it.toPost() } }

    // 댓글 올리기
    override suspend fun addComment(postId: String, user: User, text: String): String {
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

//        commentRef.set(comment.toDto()).await()
        db.runBatch { batch ->
            batch.set(commentRef, comment.toDto())
            batch.update(postRef, "commentCount", FieldValue.increment(1))
        }.await()
        return commentRef.id
    }

    // 댓글 삭제
    suspend fun deleteComment(postId: String, commentId: String) {
        val postRef = boardRef.document(postId)
        val commentRef = postRef.collection("comments").document()

        db.runBatch { batch ->
            batch.delete(commentRef)
            batch.update(postRef, "commentCount", FieldValue.increment(-1))
        }.await()
    }

    // 댓글 목록
    override fun observeComments(postId: String): Flow<List<Comment>> =
        boardRef.document(postId)
            .collection("comments")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .snapshotsFlow()
            .map { qs -> qs.documents.mapNotNull { it.toComment() } }

    // 좋아요 올리기
    override suspend fun incrementLike(postId: String, delta: Int) {
        val ref = boardRef.document(postId)
        db.runTransaction { tx ->
            val cur = (tx.get(ref).getLong("likeCount") ?: 0L).toInt()
            tx.update(ref, "likeCount", (cur + delta).coerceAtLeast(0))
        }.await()
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
}