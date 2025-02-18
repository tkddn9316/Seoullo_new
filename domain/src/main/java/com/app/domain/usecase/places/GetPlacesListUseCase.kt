package com.app.domain.usecase.places

import androidx.paging.PagingData
import com.app.domain.model.Places
import com.app.domain.model.common.ApiState
import com.app.domain.repository.PlacesRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetPlacesListUseCase @Inject constructor(private val repository: PlacesRepository) {

    operator fun invoke(
        serviceUrl: String,
        serviceKey: String,
        contentTypeId: String,
        category: String
    ): Flow<PagingData<Places>> = repository.getPlacesList(
        serviceUrl = serviceUrl,
        serviceKey = serviceKey,
        contentTypeId = contentTypeId,
        category = category
    )

    fun getBannerData(
        serviceUrl: String,
        serviceKey: String,
        contentTypeId: String,
        category: String
    ): Flow<ApiState<List<Places>>> = flow {
        emit(ApiState.Loading())
        try {
            repository.getBannerData(
                serviceUrl = serviceUrl,
                serviceKey = serviceKey,
                contentTypeId = contentTypeId,
                category = category
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