package com.app.domain.usecase.direction

import com.app.domain.model.ReverseGeocoding
import com.app.domain.model.common.ApiState
import com.app.domain.repository.ReverseGeocodingRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetReverseGeocodingUseCase @Inject constructor(private val repository: ReverseGeocodingRepository) {
    operator fun invoke(
        latLng: String,
        languageCode: String,
        apiKey: String
    ): Flow<ApiState<ReverseGeocoding>> = flow {
        emit(ApiState.Loading())
        try {
            repository.getReverseGeocoding(
                latLng = latLng,
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