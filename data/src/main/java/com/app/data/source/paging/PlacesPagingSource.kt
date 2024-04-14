package com.app.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.data.model.PlacesResponseDTO
import com.app.data.source.PlacesDataSource
import com.app.data.utils.Logging
import kotlinx.coroutines.flow.single
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// 참고: https://labs.brandi.co.kr/2021/07/07/parkks2.html
class PlacesPagingSource @Inject constructor(
    private val placesDataSource: PlacesDataSource,
    private val serviceKey: String,
    private val contentTypeId: String
) : PagingSource<Int, PlacesResponseDTO.Place>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlacesResponseDTO.Place> {
        return try {
            val currentPage = params.key ?: 1
            val response = placesDataSource.getPlacesList(
                pageNo = currentPage,
                serviceKey = serviceKey,
                contentTypeId = contentTypeId
            )
            val places = response.single().response.body.items.items
            Logging.e(places)

            // 다음 페이지 번호 계산
            val nextPage = currentPage + 1

            LoadResult.Page(
                data = places,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (places.isNotEmpty()) nextPage else null
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    // RefreshKey는 첫 번째 페이지 번호를 반환하여 새로고침할 때 첫 페이지로 돌아갈 수 있도록 함
    override fun getRefreshKey(state: PagingState<Int, PlacesResponseDTO.Place>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}