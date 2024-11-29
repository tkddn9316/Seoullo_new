package com.app.domain.usecase.places

import androidx.paging.PagingData
import com.app.domain.model.Places
import com.app.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlacesListUseCase @Inject constructor(private val repository: PlacesRepository) {

    operator fun invoke(
        serviceKey: String,
        contentTypeId: String,
        category: String
    ): Flow<PagingData<Places>> = repository.getPlacesList(
        serviceKey = serviceKey,
        contentTypeId = contentTypeId,
        category = category
    )
}