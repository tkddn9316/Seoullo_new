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
                apiKey,
                mapperToPlaceNearbyDTO(placesNearbyRequest)
            )
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
            data.place.forEachIndexed { index, place ->
                // 구글에 등록된 사진이 최소 1장 이상 있을 경우
                if (!place.photos.isNullOrEmpty()) {
                    // 무조건 1번째 사진 사용
                    placesPhotoNearbyDataSource.getPlacePhotoNearby(place.photos[0].name, key)
                        .collect { response ->
                            data.place[index].photoUrl = response.photoUri
                        }
                } else {
                    data.place[index].photoUrl = ""
                }
            }

            emit(data)
        }
    }
}