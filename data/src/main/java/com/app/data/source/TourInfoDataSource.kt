package com.app.data.source

import com.app.data.model.TourInfoResponse
import kotlinx.coroutines.flow.Flow

interface TourInfoDataSource {
    fun getTourInfo(
        serviceKey: String,
        contentTypeId: String,
        mapX: String,
        mapY: String,
        radius: Int
    ): Flow<TourInfoResponse>
}