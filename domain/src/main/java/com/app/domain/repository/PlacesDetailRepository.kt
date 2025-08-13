package com.app.domain.repository

import com.app.domain.model.PlacesDetail
import kotlinx.coroutines.flow.Flow

interface PlacesDetailRepository {
    fun getPlacesDetail(
        serviceUrl: String,
        serviceKey: String,
        contentId: String
    ): Flow<PlacesDetail>
}