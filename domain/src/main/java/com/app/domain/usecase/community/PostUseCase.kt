package com.app.domain.usecase.community

import android.net.Uri
import android.util.Log
import com.app.domain.model.DeletionResult
import com.app.domain.model.User
import com.app.domain.model.common.ApiState
import com.app.domain.repository.BoardRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class PostUseCase @Inject constructor(private val repository: BoardRepository) {
    fun addPost(
        user: User,
        title: String,
        content: String,
        images: List<Uri>
    ): Flow<ApiState<String>> = flow {
        emit(ApiState.Loading())

        if (title.isBlank()) {
            emit(ApiState.Error("제목을 입력하세요.")); return@flow
        }
        if (content.isBlank()) {
            emit(ApiState.Error("내용을 입력하세요.")); return@flow
        }

        try {
            repository.addPost(
                user = user,
                title = title,
                content = content,
                images = images
            ).collect {
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

    fun updatePost(
        user: User,
        postId: String,
        title: String,
        content: String,
        keptRemoteUrls: List<String>,
        newLocalUris: List<Uri>,
        deleteRemoved: Boolean
    ): Flow<ApiState<String>> = flow {
        emit(ApiState.Loading())

        repository.updatePost(
            user = user,
            postId = postId,
            title = title,
            content = content,
            keptRemoteUrls = keptRemoteUrls,
            newLocalUris = newLocalUris,
            deleteRemoved = deleteRemoved
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


    fun deletePost(postId: String): Flow<ApiState<DeletionResult>> = flow {
        emit(ApiState.Loading())
        val r = repository.deletePost(postId)
        r.fold(
            onSuccess = { emit(ApiState.Success(it)) },
            onFailure = { e ->
                val msg = when (e) {
                    is IOException -> {
                        "Server error: ${e.message}"
                    }
                    else -> e.message ?: "Unknown error"
                }
                Log.e("deletePost ERROR", msg)
                emit(ApiState.Error(msg))
            }
        )
    }
}