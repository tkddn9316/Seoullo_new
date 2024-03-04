package com.app.domain.usecase.tourInfo

import com.app.domain.model.TourInfo
import com.app.domain.repository.TourInfoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTourInfoUseCase @Inject constructor(private val repository: TourInfoRepository) {

    operator fun invoke(
        serviceKey: String,
        contentTypeId: String,
        mapX: String,
        mapY: String,
        radius: Int
    ): Flow<List<TourInfo>> = repository.getTourInfo(
        serviceKey = serviceKey,
        contentTypeId = contentTypeId,
        mapX = mapX,
        mapY = mapY,
        radius = radius
    )
}