package com.app.data.source

import com.app.data.api.ApiInterface
import com.app.data.model.PlacesPhotoNearbyResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlacesPhotoNearbyDataSourceImpl @Inject constructor(private val apiInterface: ApiInterface) :
    PlacesPhotoNearbyDataSource {
    override fun getPlacePhotoNearby(
        name: String,
        key: String
    ): Flow<PlacesPhotoNearbyResponseDTO> {
        return flow {
            emit(
                apiInterface.getPlacePhotoNearby(
                    name = name,
                    key = key
                )
            )
        }
    }
}