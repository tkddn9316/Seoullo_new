package com.app.domain.repository

import androidx.paging.PagingData
import com.app.domain.model.Places
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {
    fun getPlacesList(
        serviceKey: String,
        contentTypeId: String
    ): Flow<PagingData<Places>>
}