package com.app.domain.repository

import com.app.domain.model.TourInfo
import kotlinx.coroutines.flow.Flow

interface TourInfoRepository {
    fun getTourInfo(
        serviceKey: String,
        contentTypeId: String,
        mapX: String,
        mapY: String,
        radius: Int
    ): Flow<List<TourInfo>>
}