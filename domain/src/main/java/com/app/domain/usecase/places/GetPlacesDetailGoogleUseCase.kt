package com.app.domain.usecase.places

import com.app.domain.model.common.ApiState
import com.app.domain.model.PlacesDetailGoogle
import com.app.domain.repository.PlacesDetailGoogleRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetPlacesDetailGoogleUseCase @Inject constructor(private val repository: PlacesDetailGoogleRepository) {
    operator fun invoke(
        apiKey: String,
        placeId: String,
        languageCode: String
    ): Flow<ApiState<PlacesDetailGoogle>> = flow {
        emit(ApiState.Loading())
        try {
            repository.getPlacesDetail(
                apiKey = apiKey,
                placeId = placeId,
                languageCode = languageCode
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