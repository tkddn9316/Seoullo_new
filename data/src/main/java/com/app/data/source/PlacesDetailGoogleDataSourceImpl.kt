package com.app.data.source

import com.app.data.api.ApiInterface
import com.app.data.model.PlacesDetailGoogleResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlacesDetailGoogleDataSourceImpl @Inject constructor(private val apiInterface: ApiInterface) :
    PlacesDetailGoogleDataSource {

    override fun getPlacesDetail(
        apiKey: String,
        placeId: String,
        languageCode: String
    ): Flow<PlacesDetailGoogleResponseDTO> {
        return flow {
            emit(
                apiInterface.getPlacesDetail(
                    apiKey = apiKey,
                    placeId = placeId,
                    languageCode = languageCode
                )
            )
        }
    }
}