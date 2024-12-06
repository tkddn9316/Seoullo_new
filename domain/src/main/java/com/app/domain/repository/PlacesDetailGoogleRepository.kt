package com.app.domain.repository

import com.app.domain.model.PlacesDetailGoogle
import kotlinx.coroutines.flow.Flow

interface PlacesDetailGoogleRepository {
    fun getPlacesDetail(
        apiKey: String,
        placeId: String,
        languageCode: String
    ): Flow<PlacesDetailGoogle>
}