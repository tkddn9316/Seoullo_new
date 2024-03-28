package com.app.data.source

import com.app.data.model.PlacesRequestDTO
import com.app.data.model.PlacesResponseDTO
import kotlinx.coroutines.flow.Flow

interface PlacesDataSource {
    fun getTourInfo(
        apiKey: String,
        placesRequestDTO: PlacesRequestDTO
    ): Flow<PlacesResponseDTO>
}