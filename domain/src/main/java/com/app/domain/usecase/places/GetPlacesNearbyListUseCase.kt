package com.app.domain.usecase.places

import com.app.domain.model.ApiState
import com.app.domain.model.Places
import com.app.domain.model.PlacesNearbyRequest
import com.app.domain.repository.PlacesNearbyRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetPlacesNearbyListUseCase @Inject constructor(private val repository: PlacesNearbyRepository) {

    operator fun invoke(
        apiKey: String,
        placesNearbyRequest: PlacesNearbyRequest
    ): Flow<ApiState<List<Places>>> = flow {
        emit(ApiState.Loading())
        try {
            repository.getPlacesNearbyList(
                apiKey = apiKey,
                placesNearbyRequest = placesNearbyRequest
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