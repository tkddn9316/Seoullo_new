package com.app.data.source

import com.app.data.api.ApiInterface
import com.app.data.model.TourInfoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TourInfoDataSourceImpl @Inject constructor(private val apiInterface: ApiInterface) :
    TourInfoDataSource {

    override fun getTourInfo(
        serviceKey: String,
        contentTypeId: String,
        mapX: String,
        mapY: String,
        radius: Int
    ): Flow<TourInfoResponse> {
        return flow {
            emit(
                apiInterface.getTourInfoList(
                    serviceKey = serviceKey,
                    contentTypeId = contentTypeId,
                    mapX = mapX,
                    mapY = mapY,
                    radius = radius
                )
            )
        }
    }
}