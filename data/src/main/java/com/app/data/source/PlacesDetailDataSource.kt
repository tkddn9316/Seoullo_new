package com.app.data.source

import com.app.data.model.PlacesDetailResponseDTO
import kotlinx.coroutines.flow.Flow

interface PlacesDetailDataSource {
    fun getPlacesDetail(
        serviceKey: String,
        contentId: String,
        contentTypeId: String
    ): Flow<PlacesDetailResponseDTO>
}