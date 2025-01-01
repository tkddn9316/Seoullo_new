package com.app.domain.repository

import com.app.domain.model.ReverseGeocoding
import kotlinx.coroutines.flow.Flow

interface ReverseGeocodingRepository {
    fun getReverseGeocoding(
        latLng: String,
        languageCode: String,
        apiKey: String
    ): Flow<ReverseGeocoding>
}