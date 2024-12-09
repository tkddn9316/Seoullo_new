package com.app.data.source

import com.app.data.api.ApiInterface2
import com.app.data.model.PlacesDetailResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlacesDetailDataSourceImpl @Inject constructor(private val apiInterface: ApiInterface2):
    PlacesDetailDataSource {

    override fun getPlacesDetail(
        serviceKey: String,
        contentId: String,
        contentTypeId: String
    ): Flow<PlacesDetailResponseDTO> {
        return flow {
            emit(
                apiInterface.getPlacesInfoDetail(
                    serviceKey = serviceKey,
                    contentId = contentId,
                    contentTypeId = contentTypeId
                )
            )
        }
    }
}