package com.app.domain.repository

import com.app.domain.model.PlacesDetail
import kotlinx.coroutines.flow.Flow

interface PlacesDetailRepository {
    fun getPlacesDetail(
        serviceKey: String,
        contentId: String,
        contentTypeId: String
    ): Flow<PlacesDetail>
}