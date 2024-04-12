package com.app.data.source

import com.app.data.model.PlacesResponseDTO
import kotlinx.coroutines.flow.Flow

interface PlacesDataSource {
    fun getPlacesList(
        pageNo: Int,
        serviceKey: String,
        contentTypeId: String
    ): Flow<PlacesResponseDTO>
}