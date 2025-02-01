package com.app.domain.usecase.places

import com.app.domain.model.PlacesAutoComplete
import com.app.domain.model.PlacesAutoCompleteRequest
import com.app.domain.model.common.ApiState
import com.app.domain.repository.AutoCompleteRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetPlacesAutoCompleteUseCase @Inject constructor(private val autoCompleteRepository: AutoCompleteRepository) {
    operator fun invoke(
        apiKey: String,
        autoCompleteRequest: PlacesAutoCompleteRequest
    ): Flow<ApiState<PlacesAutoComplete>> = flow {
        emit(ApiState.Loading())
        try {
            autoCompleteRepository.getAutoComplete(
                apiKey = apiKey,
                autoCompleteRequest = autoCompleteRequest
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