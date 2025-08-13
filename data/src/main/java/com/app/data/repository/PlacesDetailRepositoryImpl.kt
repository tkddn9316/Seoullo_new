package com.app.data.repository

import com.app.data.mapper.mapperToPlaceDetail
import com.app.data.source.PlacesDetailDataSource
import com.app.domain.model.PlacesDetail
import com.app.domain.repository.PlacesDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlacesDetailRepositoryImpl @Inject constructor(
    private val placesDetailDataSource: PlacesDetailDataSource
): PlacesDetailRepository {

    override fun getPlacesDetail(
        serviceUrl: String,
        serviceKey: String,
        contentId: String
    ): Flow<PlacesDetail> {
        return flow {
            placesDetailDataSource.getPlacesDetail(
                serviceUrl = serviceUrl,
                serviceKey = serviceKey,
                contentId = contentId
            ).collect {
                emit(mapperToPlaceDetail((it.response.body.items?.items?: emptyList())[0]))
            }
        }
    }
}