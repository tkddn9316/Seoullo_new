package com.app.data.repository

import com.app.data.mapper.mapperToPlaceNearby
import com.app.data.mapper.mapperToPlaceNearbyDTO
import com.app.data.source.PlacesNearbyDataSource
import com.app.domain.model.Places
import com.app.domain.model.PlacesNearbyRequest
import com.app.domain.repository.PlacesNearbyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * TourInfoRepository의 구현부. TourInfoRepository는 Domain 계층에 있다.
 * (Data는 Domain에 의존적)
 */
class PlacesNearbyRepositoryImpl @Inject constructor(private val placesNearbyDataSource: PlacesNearbyDataSource) :
    PlacesNearbyRepository {

    override fun getPlacesNearbyList(
        apiKey: String,
        placesNearbyRequest: PlacesNearbyRequest
    ): Flow<List<Places>> {
        return flow {
            placesNearbyDataSource.getPlacesNearbyList(apiKey, mapperToPlaceNearbyDTO(placesNearbyRequest))
//                .catch { exception ->
//                    emit(mapperTOTourInfo(emptyList()))
//                }
                .collect {
                    // mapper로 변환하기(TourInfoDTO -> TourInfo)
                    emit(mapperToPlaceNearby(it))
                }
        }
    }
}