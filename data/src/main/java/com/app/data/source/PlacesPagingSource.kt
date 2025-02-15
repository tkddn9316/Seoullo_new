package com.app.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.data.model.PlacesResponseDTO
import com.app.data.utils.Logging
import kotlinx.coroutines.flow.single
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PlacesPagingSource @Inject constructor(
    private val placesDataSource: PlacesDataSource,
    private val serviceUrl: String,
    private val serviceKey: String,
    private val contentTypeId: String,
    private val category: String
) : PagingSource<Int, PlacesResponseDTO.Place>() {

    private var totalPageCount: Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlacesResponseDTO.Place> {
        return try {
            // 카테고리 나누기
            val (category1, category2, category3) = splitCategory(category)

            val currentPage = params.key ?: 1
            Logging.d("${javaClass.name}: $currentPage")
            val response = placesDataSource.getPlacesList(
                serviceUrl = serviceUrl,
                pageNo = currentPage,
                serviceKey = serviceKey,
                contentTypeId = contentTypeId,
                category1 = category1,
                category2 = category2,
                category3 = category3
            )
            val body = response.single().response.body
            val places = body.items?.items ?: emptyList()   // 데이터
            if (totalPageCount == null) {
                // 총 페이지 수 계산
                totalPageCount = if (body.numOfRows > 0) {
                    (body.totalCount + body.numOfRows - 1) / body.numOfRows
                } else {
                    1
                }
            }
            Logging.d("${javaClass.name}: $totalPageCount")

            // 다음 페이지 번호 계산
            val nextPage = if (currentPage < totalPageCount!!) currentPage + 1 else null

            LoadResult.Page(
                data = places,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = nextPage
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

    private fun splitCategory(input: String): Triple<String, String, String> {
        return when {
            input.length >= 9 -> Triple(input.substring(0, 3), input.substring(0, 5), input)
            input.length >= 5 -> Triple(input.substring(0, 3), input.substring(0, 5), "")
            input.length >= 3 -> Triple(input.substring(0, 3), "", "")
            else -> Triple("", "", "")
        }
    }
}