package com.app.domain.usecase.community

import com.app.domain.model.User
import com.app.domain.model.common.ApiState
import com.app.domain.repository.BoardRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class LikePostUseCase @Inject constructor(private val boardRepository: BoardRepository) {
    suspend fun hasLiked(postId: String, user: User): Result<Boolean> = runCatching {
        boardRepository.hasLiked(postId = postId, user = user)
    }

    //    suspend fun like(postId: String, user: User): Result<Unit> = runCatching {
//        boardRepository.like(postId = postId, user = user)
//    }
    fun like(postId: String, user: User): Flow<ApiState<Unit>> = flow {
        emit(ApiState.Loading())

        boardRepository.like(postId = postId, user = user)
            .collect {
                emit(ApiState.Success(Unit))
            }
    }.catch { e ->
        val errorMessage = when (e) {
            is IOException -> "Network Error: ${e.message}"
            is JsonSyntaxException -> "Parsing error: Received non-JSON response (possibly HTML)."
            else -> "Exception: ${e.message}"
        }
        emit(ApiState.Error(errorMessage))
    }

    //    suspend fun unlike(postId: String, user: User): Result<Unit> = runCatching {
//        boardRepository.unlike(postId = postId, user = user)
//    }
    fun unlike(postId: String, user: User): Flow<ApiState<Unit>> = flow {
        emit(ApiState.Loading())

        boardRepository.unlike(postId = postId, user = user)
            .collect {
                emit(ApiState.Success(Unit))
            }
    }.catch { e ->
        val errorMessage = when (e) {
            is IOException -> "Network Error: ${e.message}"
            is JsonSyntaxException -> "Parsing error: Received non-JSON response (possibly HTML)."
            else -> "Exception: ${e.message}"
        }
        emit(ApiState.Error(errorMessage))
    }
}