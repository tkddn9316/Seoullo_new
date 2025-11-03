package com.app.domain.repository

import android.net.Uri
import com.app.domain.model.User
import com.app.domain.model.community.Comment
import com.app.domain.model.community.Post
import kotlinx.coroutines.flow.Flow

interface BoardRepository {
    fun addPost(user: User, title: String, content: String, images: List<Uri>): Flow<String>
    fun observePosts(): Flow<List<Post>>
    fun getPost(postId: String): Flow<Post?>

    suspend fun addComment(postId: String, user: User, text: String): Flow<String>
    suspend fun deleteComment(postId: String, commentId: String): Flow<Unit>
    suspend fun addReply(postId: String, commentId: String, user: User, text: String): Flow<String>
    suspend fun deleteReply(postId: String, commentId: String, replyId: String): Flow<Unit>
    fun observeCommentsWithReplies(postId: String): Flow<List<Comment>>

//    suspend fun incrementLike(postId: String, delta: Int)
    suspend fun hasLiked(postId: String, user: User): Boolean
    suspend fun like(postId: String, user: User)
    suspend fun unlike(postId: String, user: User)
}