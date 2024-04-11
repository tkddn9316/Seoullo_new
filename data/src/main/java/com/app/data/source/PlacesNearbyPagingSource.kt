package com.app.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.data.model.PlacesNearbyResponseDTO
import javax.inject.Inject

//class PlacesNearbyPagingSource @Inject constructor(
//    private val placesNearbyDataSource: PlacesNearbyDataSource
//) : PagingSource<Int, PlacesNearbyResponseDTO>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlacesNearbyResponseDTO> {
//        return try {
//            val currentPage = params.key ?: 1
//            val response = placesNearbyDataSource.getPlacesNearbyList(
//                apiKey =
//            )
//
//            LoadResult.Page(
//                data = response.place
//            )
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, PlacesNearbyResponseDTO>): Int? {
//
//    }
//}