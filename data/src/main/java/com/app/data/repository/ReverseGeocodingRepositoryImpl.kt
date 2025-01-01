package com.app.data.repository

import com.app.data.mapper.mapperToReverseGeocoding
import com.app.data.source.ReverseGeocodingDataSource
import com.app.domain.model.ReverseGeocoding
import com.app.domain.repository.ReverseGeocodingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReverseGeocodingRepositoryImpl @Inject constructor(
    private val reverseGeocodingDataSource: ReverseGeocodingDataSource
) : ReverseGeocodingRepository {
    override fun getReverseGeocoding(
        latLng: String,
        languageCode: String,
        apiKey: String
    ): Flow<ReverseGeocoding> {
        return flow {
            reverseGeocodingDataSource.getReverseGeocoding(
                latLng = latLng,
                languageCode = languageCode,
                apiKey = apiKey
            ).collect {
                emit(mapperToReverseGeocoding(it))
            }
        }
    }
}