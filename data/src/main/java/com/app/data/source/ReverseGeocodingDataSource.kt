package com.app.data.source

import com.app.data.model.ReverseGeocodingDTO
import kotlinx.coroutines.flow.Flow

interface ReverseGeocodingDataSource {
    fun getReverseGeocoding(
        latLng: String,
        languageCode: String,
        apiKey: String
    ): Flow<ReverseGeocodingDTO>
}