package com.app.data.source

import com.app.data.model.PlacesResponseDTO
import kotlinx.coroutines.flow.Flow

interface PlacesDataSource {
    fun getPlacesList(
        serviceUrl: String,
        pageNo: Int,
        serviceKey: String,
        contentTypeId: String,
        category: String
    ): Flow<PlacesResponseDTO>
}