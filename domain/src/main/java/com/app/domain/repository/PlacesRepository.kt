package com.app.domain.repository

import androidx.paging.PagingData
import com.app.domain.model.Places
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {
    fun getPlacesList(
        serviceUrl: String,
        serviceKey: String,
        contentTypeId: String,
        category: String
    ): Flow<PagingData<Places>>
}