package com.app.domain.usecase.community

import com.app.domain.model.User
import com.app.domain.model.common.ApiState
import com.app.domain.repository.BoardRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class AddPostUseCase @Inject constructor(private val repository: BoardRepository) {
    operator fun invoke(
        user: User,
        title: String,
        content: String,
        images: List<ByteArray>
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
}