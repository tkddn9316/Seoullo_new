package com.app.data.repository

import com.app.data.source.TourInfoDataSource
import com.app.data.mapper.mapperTOTourInfo
import com.app.domain.model.TourInfo
import com.app.domain.repository.TourInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * TourInfoRepository의 구현부. TourInfoRepository는 Domain 계층에 있다.
 * (Data는 Domain에 의존적)
 */
class TourInfoRepositoryImpl @Inject constructor(private val tourInfoDataSource: TourInfoDataSource) :
    TourInfoRepository {

    // Data 계층에서 여행 리스트 가져오기
    override fun getTourInfo(
        serviceKey: String,
        contentTypeId: String,
        mapX: String,
        mapY: String,
        radius: Int
    ): Flow<List<TourInfo>> {
        return flow {
            tourInfoDataSource.getTourInfo(serviceKey, contentTypeId, mapX, mapY, radius)
//                .catch { exception ->
//                    emit(mapperTOTourInfo(emptyList()))
//                }
                .map { it.response.body.items.items }
                .filter { it.isNotEmpty() }
                .collect {
                    // mapper로 변환하기(TourInfoDTO -> TourInfo)
                    emit(mapperTOTourInfo(it))
                }
        }
    }
}