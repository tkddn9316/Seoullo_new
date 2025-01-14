package com.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.app.data.mapper.mapperToPlace
import com.app.data.source.PlacesDataSource
import com.app.data.source.PlacesPagingSource
import com.app.domain.model.Places
import com.app.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val placesDataSource: PlacesDataSource
) : PlacesRepository {

    companion object {
        private const val PAGE_SIZE = 10
    }

    override fun getPlacesList(
        serviceUrl: String,
        serviceKey: String,
        contentTypeId: String,
        category: String
    ): Flow<PagingData<Places>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                PlacesPagingSource(
                    placesDataSource = placesDataSource,
                    serviceUrl = serviceUrl,
                    serviceKey = serviceKey,
                    contentTypeId = contentTypeId,
                    category = category
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { placesResponseDTO ->
                mapperToPlace(placesResponseDTO)
            }
        }
    }
}