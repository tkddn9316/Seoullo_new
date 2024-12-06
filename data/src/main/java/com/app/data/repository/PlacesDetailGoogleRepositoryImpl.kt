package com.app.data.repository

import com.app.data.mapper.mapperToPlaceDetailGoogle
import com.app.data.source.PlacesDetailGoogleDataSource
import com.app.domain.model.PlacesDetailGoogle
import com.app.domain.repository.PlacesDetailGoogleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlacesDetailGoogleRepositoryImpl @Inject constructor(
    private val placesDetailGoogleDataSource: PlacesDetailGoogleDataSource
) : PlacesDetailGoogleRepository {

    override fun getPlacesDetail(
        apiKey: String,
        placeId: String,
        languageCode: String
    ): Flow<PlacesDetailGoogle> {
        return flow {
            placesDetailGoogleDataSource.getPlacesDetail(
                apiKey, placeId, languageCode
            ).collect {
                emit(mapperToPlaceDetailGoogle(it))
            }
        }
    }
}