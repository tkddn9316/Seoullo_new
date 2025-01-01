package com.app.data.source

import com.app.data.api.ApiInterface
import com.app.data.model.ReverseGeocodingDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReverseGeocodingDataSourceImpl @Inject constructor(private val apiInterface: ApiInterface) :
    ReverseGeocodingDataSource {

    override fun getReverseGeocoding(
        latLng: String,
        languageCode: String,
        apiKey: String
    ): Flow<ReverseGeocodingDTO> {
        return flow {
            emit(
                apiInterface.getReverseGeocoding(
                    latLng = latLng,
                    languageCode = languageCode,
                    apiKey = apiKey
                )
            )
        }
    }
}