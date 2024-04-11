package com.app.domain.repository

import com.app.domain.model.PlacesNearbyRequest
import com.app.domain.model.Places
import kotlinx.coroutines.flow.Flow

interface PlacesNearbyRepository {
    fun getPlacesNearbyList(
        apiKey: String,
        placesNearbyRequest: PlacesNearbyRequest
    ): Flow<List<Places>>
}