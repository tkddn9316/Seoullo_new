package com.app.data.source

import com.app.data.model.PlacesPhotoNearbyResponseDTO
import kotlinx.coroutines.flow.Flow

interface PlacesPhotoNearbyDataSource {
    fun getPlacePhotoNearby(
        name: String,
        key: String
    ): Flow<PlacesPhotoNearbyResponseDTO>
}