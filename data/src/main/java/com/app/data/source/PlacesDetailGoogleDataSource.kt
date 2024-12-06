package com.app.data.source

import com.app.data.model.PlacesDetailGoogleResponseDTO
import kotlinx.coroutines.flow.Flow

interface PlacesDetailGoogleDataSource {
    fun getPlacesDetail(
        apiKey: String,
        placeId: String,
        languageCode: String
    ): Flow<PlacesDetailGoogleResponseDTO>
}