package com.app.data.source

import com.app.data.api.ApiInterface2
import com.app.data.model.PlacesResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlacesDataSourceImpl @Inject constructor(private val apiInterface: ApiInterface2) :
    PlacesDataSource {

    override fun getPlacesList(
        pageNo: Int,
        serviceKey: String,
        contentTypeId: String,
        category: String
    ): Flow<PlacesResponseDTO> {
        return flow {
            emit(
                apiInterface.getPlacesList(
                    pageNo = pageNo,
                    serviceKey = serviceKey,
                    contentTypeId = contentTypeId,
                    category = category
                )
            )
        }
    }
}