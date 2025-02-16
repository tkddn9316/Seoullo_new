package com.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.app.data.mapper.mapperToBanner
import com.app.data.mapper.mapperToPlace
import com.app.data.source.PlacesDataSource
import com.app.data.source.PlacesPagingSource
import com.app.data.utils.Util.splitCategory
import com.app.domain.model.Places
import com.app.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    override fun getBannerData(
        serviceUrl: String,
        serviceKey: String,
        contentTypeId: String,
        category: String
    ): Flow<List<Places>> {
        val (category1, category2, category3) = splitCategory(category)

        return flow {
            placesDataSource.getPlacesList(
                serviceUrl = serviceUrl,
                pageNo = 1,
                serviceKey = serviceKey,
                contentTypeId = contentTypeId,
                category1 = category1,
                category2 = category2,
                category3 = category3
            ).collect {
                val body = it.response.body
                val places = body.items?.items ?: emptyList()
                // mapper로 변환하기(TourInfoDTO -> TourInfo)
                emit(mapperToBanner(places))
            }
        }
    }
}