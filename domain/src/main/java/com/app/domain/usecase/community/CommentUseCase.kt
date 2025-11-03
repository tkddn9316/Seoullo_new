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

class CommentUseCase @Inject constructor(private val boardRepository: BoardRepository) {
    fun addComment(
        postId: String,
        user: User,
        text: String
    ): Flow<ApiState<String>> = flow {
        emit(ApiState.Loading())

        if (text.isBlank()) {
            emit(ApiState.Error("댓글을 입력하세요.")); return@flow
        }

        boardRepository.addComment(
            postId = postId,
            user = user,
            text = text
        ).collect {
            emit(ApiState.Success(it))
        }
    }.catch { e ->
        val errorMessage = when (e) {
            is IOException -> "Network Error: ${e.message}"
            is JsonSyntaxException -> "Parsing error: Received non-JSON response (possibly HTML)."
            else -> "Exception: ${e.message}"
        }
        emit(ApiState.Error(errorMessage))
    }

    fun deleteComment(
        postId: String,
        commentId: String
    ): Flow<ApiState<Unit>> = flow {
        emit(ApiState.Loading())

        boardRepository.deleteComment(
            postId = postId,
            commentId = commentId
        ).collect {
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

    fun addReply(
        postId: String,
        commentId: String,
        user: User,
        text: String
    ): Flow<ApiState<String>> = flow {
        emit(ApiState.Loading())

        if (text.isBlank()) {
            emit(ApiState.Error("답글을 입력하세요.")); return@flow
        }

        boardRepository.addReply(
            postId = postId,
            commentId = commentId,
            user = user,
            text = text
        ).collect {
            emit(ApiState.Success(it))
        }
    }.catch { e ->
        val errorMessage = when (e) {
            is IOException -> "Network Error: ${e.message}"
            is JsonSyntaxException -> "Parsing error: Received non-JSON response (possibly HTML)."
            else -> "Exception: ${e.message}"
        }
        emit(ApiState.Error(errorMessage))
    }

    fun deleteReply(
        postId: String,
        commentId: String,
        replyId: String
    ): Flow<ApiState<Unit>> = flow {
        emit(ApiState.Loading())

        boardRepository.deleteReply(
            postId = postId,
            commentId = commentId,
            replyId = replyId
        ).collect {
            emit(ApiState.Success(it))
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