package com.app.data.repository

import com.app.data.mapper.mapperToTodayWatchedList
import com.app.data.model.TodayWatchedListEntity
import com.app.data.source.TodayWatchedListDataSource
import com.app.domain.model.TodayWatchedList
import com.app.domain.repository.TodayWatchedListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TodayWatchedListRepositoryImpl @Inject constructor(private val todayWatchedListDataSource: TodayWatchedListDataSource) :
    TodayWatchedListRepository {
    override suspend fun insertWatchedItem(item: TodayWatchedList) {
        val data = TodayWatchedListEntity(
            isNearby = item.isNearby,
            languageCode = item.languageCode,
            name = item.name,
            id = item.id,
            contentTypeId = item.contentTypeId,
            displayName = item.displayName,
            address = item.address,
            description = item.description,
            openNow = item.openNow,
            weekdayDescriptions = item.weekdayDescriptions,
            rating = item.rating,
            userRatingCount = item.userRatingCount,
            photoUrl = item.photoUrl
        )
        todayWatchedListDataSource.insertWatchedItem(data)
    }

    override suspend fun insertAllWatchedItems(items: List<TodayWatchedList>) {

    }

    override fun getAllWatchedItems(): Flow<List<TodayWatchedList>> {
        return flow {
            todayWatchedListDataSource.getAllWatchedItems().collect {
                emit(mapperToTodayWatchedList(it))
            }
        }
    }

    override suspend fun clearWatchedList() {
        todayWatchedListDataSource.clearWatchedList()
    }
}