package com.app.domain.usecase.places

import com.app.domain.model.ApiState
import com.app.domain.model.PlacesDetail
import com.app.domain.repository.PlacesDetailRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetPlacesDetailUseCase @Inject constructor(private val repository: PlacesDetailRepository) {
    operator fun invoke(
        serviceUrl: String,
        serviceKey: String,
        contentId: String,
        contentTypeId: String
    ): Flow<ApiState<PlacesDetail>> = flow {
        emit(ApiState.Loading())
        try {
            repository.getPlacesDetail(
                serviceUrl = serviceUrl,
                serviceKey = serviceKey,
                contentId = contentId,
                contentTypeId = contentTypeId
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