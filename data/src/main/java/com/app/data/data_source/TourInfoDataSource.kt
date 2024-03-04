package com.app.data.data_source

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