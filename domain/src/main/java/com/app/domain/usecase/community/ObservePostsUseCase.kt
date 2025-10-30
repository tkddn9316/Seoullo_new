package com.app.domain.usecase.community

import com.app.domain.model.common.ApiState
import com.app.domain.model.community.Post
import com.app.domain.repository.BoardRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class ObservePostsUseCase @Inject constructor(private val repository: BoardRepository) {
//    operator fun invoke() = boardRepository.observePosts()

    operator fun invoke(): Flow<ApiState<List<Post>>> = flow {
        emit(ApiState.Loading())
        try {
            repository.observePosts().collect {
                emit(ApiState.Success(it))
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is IOException -> "Network Error: ${e.message}"
                is JsonSyntaxException -> "Parsing error: Received non-JSON response (possibly HTML)."
                else -> "Exception: ${e.message}"
            }
            emit(ApiState.Error(errorMessage))
        }
    }

    fun getPost(postId: String): Flow<ApiState<Post>> = flow {
        emit(ApiState.Loading())
        try {
            repository.getPost(postId = postId).collect { post ->
                post?.let {
                    emit(ApiState.Success(it))
                } ?: run {
                    emit(ApiState.Error("Post not found"))
                }
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is IOException -> "Network Error: ${e.message}"
                is JsonSyntaxException -> "Parsing error: Received non-JSON response (possibly HTML)."
                else -> "Exception: ${e.message}"
            }
            emit(ApiState.Error(errorMessage))
        }
    }
}