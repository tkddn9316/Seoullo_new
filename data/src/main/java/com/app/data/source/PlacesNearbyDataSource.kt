package com.app.data.source

import com.app.data.model.PlacesNearbyRequestDTO
import com.app.data.model.PlacesNearbyResponseDTO
import kotlinx.coroutines.flow.Flow

interface PlacesNearbyDataSource {
    fun getPlacesNearbyList(
        apiKey: String,
        placesNearbyRequestDTO: PlacesNearbyRequestDTO
    ): Flow<PlacesNearbyResponseDTO>
}