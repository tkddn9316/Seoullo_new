package com.app.domain.usecase.places

import com.app.domain.model.Places
import com.app.domain.model.PlacesNearbyRequest
import com.app.domain.repository.PlacesNearbyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlacesNearbyListUseCase @Inject constructor(private val repository: PlacesNearbyRepository) {

    operator fun invoke(
        apiKey: String,
        placesNearbyRequest: PlacesNearbyRequest
    ): Flow<List<Places>> = repository.getPlacesNearbyList(
        apiKey = apiKey,
        placesNearbyRequest = placesNearbyRequest
    )
}