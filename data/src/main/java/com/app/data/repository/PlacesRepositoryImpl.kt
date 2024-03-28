package com.app.data.repository

import com.app.data.source.PlacesDataSource
import com.app.data.mapper.mapperToPlace
import com.app.data.mapper.mapperToPlaceDTO
import com.app.domain.model.Places
import com.app.domain.model.PlacesRequest
import com.app.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * TourInfoRepository의 구현부. TourInfoRepository는 Domain 계층에 있다.
 * (Data는 Domain에 의존적)
 */
class PlacesRepositoryImpl @Inject constructor(private val placesDataSource: PlacesDataSource) :
    PlacesRepository {

    override fun getTourInfo(
        apiKey: String,
        placesRequest: PlacesRequest
    ): Flow<List<Places>> {
        return flow {
            placesDataSource.getTourInfo(apiKey, mapperToPlaceDTO(placesRequest))
//                .catch { exception ->
//                    emit(mapperTOTourInfo(emptyList()))
//                }
                .collect {
                    // mapper로 변환하기(TourInfoDTO -> TourInfo)
                    emit(mapperToPlace(it))
                }
        }
    }
}