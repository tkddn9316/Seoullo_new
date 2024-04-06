package com.app.data.repository

import com.app.data.mapper.mapperToPlaceNearby
import com.app.data.mapper.mapperToPlaceNearbyDTO
import com.app.data.model.PlacesNearbyResponseDTO
import com.app.data.source.PlacesNearbyDataSource
import com.app.data.source.PlacesPhotoNearbyDataSource
import com.app.domain.model.Places
import com.app.domain.model.PlacesNearbyRequest
import com.app.domain.repository.PlacesNearbyRepository
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
) :
    PlacesNearbyRepository {

    override fun getPlacesNearbyList(
        apiKey: String,
        placesNearbyRequest: PlacesNearbyRequest
    ): Flow<List<Places>> {
        return flow {
            placesNearbyDataSource.getPlacesNearbyList(
                apiKey,
                mapperToPlaceNearbyDTO(placesNearbyRequest)
            )
//                .catch { exception ->
//                    emit(mapperTOTourInfo(emptyList()))
//                }
                .flatMapConcat { getPhotoUrl(apiKey, it) }
                .collect {
                    // mapper로 변환하기(TourInfoDTO -> TourInfo)
                    emit(mapperToPlaceNearby(it))
                }
        }
    }

    private suspend fun getPhotoUrl(
        key: String,
        data: PlacesNearbyResponseDTO
    ): Flow<PlacesNearbyResponseDTO> {
        return flow {
            if (data.place.any { place -> !place.photos.isNullOrEmpty() }) {
                data.place.forEachIndexed { index, place ->
                    // 무조건 1번째 사진 사용
                    placesPhotoNearbyDataSource.getPlacePhotoNearby(place.photos!![0].name, key)
                        .collect { response ->
                            data.place[index].photoUrl = response.photoUri
                        }
                }
            }

            emit(data)
        }
    }
}