package com.app.data.source

import com.app.data.api.ApiInterface
import com.app.data.model.PlacesRequestDTO
import com.app.data.model.PlacesResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlacesDataSourceImpl @Inject constructor(private val apiInterface: ApiInterface) :
    PlacesDataSource {

    override fun getTourInfo(
        apiKey: String,
        placesRequestDTO: PlacesRequestDTO
    ): Flow<PlacesResponseDTO> {
        return flow {
            emit(
                apiInterface.getPlacesList(
                    apiKey = apiKey,
                    placesRequestDTO = placesRequestDTO
                )
            )
        }
    }
}