package com.app.domain.repository

import com.app.domain.model.PlacesRequest
import com.app.domain.model.Places
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {
    fun getTourInfo(
        apiKey: String,
        placesRequest: PlacesRequest
    ): Flow<List<Places>>
}