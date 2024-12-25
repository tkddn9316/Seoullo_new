package com.app.domain.usecase.direction

import com.app.domain.model.Direction
import com.app.domain.model.common.ApiState
import com.app.domain.repository.DirectionRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetDirectionUseCase @Inject constructor(private val repository: DirectionRepository) {
    operator fun invoke(
        destination: String,
        starting: String,
        languageCode: String,
        apiKey: String
    ): Flow<ApiState<Direction>> = flow {
        emit(ApiState.Loading())
        try {
            repository.getDirection(
                destination = destination,
                starting = starting,
                languageCode = languageCode,
                apiKey = apiKey
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