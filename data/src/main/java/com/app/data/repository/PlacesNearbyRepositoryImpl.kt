package com.app.data.repository

import com.app.data.mapper.mapperToPlaceNearby
import com.app.data.mapper.mapperToPlaceNearbyDTO
import com.app.data.model.PlacesNearbyResponseDTO
import com.app.data.source.PlacesNearbyDataSource
import com.app.data.source.PlacesPhotoNearbyDataSource
import com.app.domain.model.Places
import com.app.domain.model.PlacesNearbyRequest
import com.app.domain.repository.PlacesNearbyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * PlacesNearbyRepository의 구현부. PlacesNearbyRepository는 Domain 계층에 있다.
 * (Data는 Domain에 의존적)
 */
class PlacesNearbyRepositoryImpl @Inject constructor(
    private val placesNearbyDataSource: PlacesNearbyDataSource,
    private val placesPhotoNearbyDataSource: PlacesPhotoNearbyDataSource
) : PlacesNearbyRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getPlacesNearbyList(
        apiKey: String,
        placesNearbyRequest: PlacesNearbyRequest
    ): Flow<List<Places>> {
        return flow {
            placesNearbyDataSource.getPlacesNearbyList(
                apiKey = apiKey,
                placesNearbyRequestDTO = mapperToPlaceNearbyDTO(placesNearbyRequest)
            ).flatMapConcat {
                getPhotoUrl(
                    apiKey = apiKey,
                    data = it
                )
            }.collect {
                // mapper로 변환하기(TourInfoDTO -> TourInfo)
                emit(mapperToPlaceNearby(it))
            }
        }
    }

    private fun getPhotoUrl(
        apiKey: String,
        data: PlacesNearbyResponseDTO
    ): Flow<PlacesNearbyResponseDTO> {
        return flow {
            data.place?.map { place ->
                if (!place.photos.isNullOrEmpty()) {
                    placesPhotoNearbyDataSource.getPlacePhotoNearby(
                        name = place.photos[0].name,    // 무조건 1번째 사진 사용
                        key = apiKey
                    ).collect { response ->
                        place.photoUrl = response.photoUri
                    }
                } else {
                    place.photoUrl = ""
                }
            } ?: run { emit(data) }

            emit(data)
        }
    }
}