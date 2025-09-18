package com.app.data.source

import com.app.data.api.ApiInterface2
import com.app.data.model.PlacesDetailResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlacesDetailDataSourceImpl @Inject constructor(private val apiInterface: ApiInterface2):
    PlacesDetailDataSource {

    override fun getPlacesDetail(
        serviceUrl: String,
        serviceKey: String,
        contentId: String
    ): Flow<PlacesDetailResponseDTO> {
        return flow {
            emit(
                apiInterface.getPlacesInfoDetail(
                    serviceUrl = serviceUrl,
                    serviceKey = serviceKey,
                    contentId = contentId
                )
            )
        }
    }
}