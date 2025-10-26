package com.app.data.repository

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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class BoardRepositoryImpl @Inject constructor(
    @Named("boardRef") private val boardRef: CollectionReference,
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : BoardRepository {
    override fun addPost(
        user: User,
        title: String,
        content: String,
        images: List<ByteArray>
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

    override fun observePosts(): Flow<List<Post>> =
        boardRef
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshotsFlow()
            .map { qs -> qs.documents.mapNotNull { it.toPost() } }

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

    suspend fun deleteComment(postId: String, commentId: String) {
        val postRef = boardRef.document(postId)
        val commentRef = postRef.collection("comments").document()

        db.runBatch { batch ->
            batch.delete(commentRef)
            batch.update(postRef, "commentCount", FieldValue.increment(-1))
        }.await()
    }

    override fun observeComments(postId: String): Flow<List<Comment>> =
        boardRef.document(postId)
            .collection("comments")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .snapshotsFlow()
            .map { qs -> qs.documents.mapNotNull { it.toComment() } }

    override suspend fun incrementLike(postId: String, delta: Int) {
        val ref = boardRef.document(postId)
        db.runTransaction { tx ->
            val cur = (tx.get(ref).getLong("likeCount") ?: 0L).toInt()
            tx.update(ref, "likeCount", (cur + delta).coerceAtLeast(0))
        }.await()
    }

//    private fun postsCol() = db.collection("posts")

    private suspend fun uploadPostImages(postId: String, images: List<ByteArray>): List<String> =
        coroutineScope {
            val base = storage.reference.child("posts/$postId")
            images.mapIndexed { i, bytes ->
                async {
                    val obj = base.child("img_$i.jpg")
                    obj.putBytes(bytes).await()
                    obj.downloadUrl.await().toString()
                }
            }.awaitAll()
        }

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