package com.app.domain.usecase.tourInfo

import com.app.domain.model.Places
import com.app.domain.model.PlacesRequest
import com.app.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlacesListUseCase @Inject constructor(private val repository: PlacesRepository) {

    operator fun invoke(
        apiKey: String,
        placesRequest: PlacesRequest
    ): Flow<List<Places>> = repository.getTourInfo(
        apiKey = apiKey,
        placesRequest = placesRequest
    )
}