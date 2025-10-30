package com.app.domain.usecase.community

import com.app.domain.model.common.ApiState
import com.app.domain.model.community.Post
import com.app.domain.repository.BoardRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.io.IOException
import javax.inject.Inject

class ObservePostsUseCase @Inject constructor(private val repository: BoardRepository) {
    fun getPostList(): Flow<ApiState<List<Post>>> =
        repository.observePosts()
            .map<List<Post>, ApiState<List<Post>>> { posts ->
                ApiState.Success(posts)
            }
            .onStart { emit(ApiState.Loading()) }
            .catch { e -> emit(ApiState.Error(e.message ?: "Unknown error")) }

    fun getPost(postId: String): Flow<ApiState<Post>> =
        repository.getPost(postId)
            .map { post ->
                post?.let { ApiState.Success(it) } ?: ApiState.Error("Post not found")
            }
            .onStart { emit(ApiState.Loading()) }
            .catch { e -> emit(ApiState.Error(e.message ?: "Unknown error")) }
}