package com.app.data.source.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.app.data.db.SeoulloDatabase
import com.app.data.db.entity.BaseEntity
import com.app.data.source.PlacesDataSource
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PlacesRemoteMediator @Inject constructor(
    private val placesDataSource: PlacesDataSource,
    private val serviceKey: String,
    private val contentTypeId: String,
    private val database: SeoulloDatabase
) : RemoteMediator<Int, BaseEntity>() {

    companion object {
        private const val PAGE_SIZE = 10
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BaseEntity>
    ): MediatorResult {
        return try {
            val pageNo = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    lastItem.pageNo + 1
                }
            }

            val response = placesDataSource.getPlacesList(
                pageNo = pageNo ?: 1,
                serviceKey = serviceKey,
                contentTypeId = contentTypeId
            )

            
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}

// 모든 페이징 데이터 보여줬거나 기존 데이터가 없을 때,
// 다음 페이지에 대한 항목들을 보여주기 위해 RemoteMediator를 통해 네트워크에서 다음 데이터를 불러옵니다.
// 그리고 가져온 데이터들을 로컬 데이터베이스(Room)에 저장하면 UI가 자동으로 업데이트되는 원리입니다.
