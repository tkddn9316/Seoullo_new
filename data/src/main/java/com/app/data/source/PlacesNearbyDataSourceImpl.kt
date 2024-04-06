package com.app.data.source

import com.app.data.api.ApiInterface
import com.app.data.model.PlacesNearbyRequestDTO
import com.app.data.model.PlacesNearbyResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlacesNearbyDataSourceImpl @Inject constructor(private val apiInterface: ApiInterface) :
    PlacesNearbyDataSource {

    override fun getPlacesNearbyList(
        apiKey: String,
        placesNearbyRequestDTO: PlacesNearbyRequestDTO
    ): Flow<PlacesNearbyResponseDTO> {
        return flow {
            emit(
                apiInterface.getPlacesNearbyList(
                    apiKey = apiKey,
                    placesNearbyRequestDTO = placesNearbyRequestDTO
                )
            )
        }
    }
}