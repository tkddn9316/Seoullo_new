package com.app.data.source

import com.app.data.db.TodayWatchedListDao
import com.app.data.model.TodayWatchedListEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodayWatchedListDataSourceImpl @Inject constructor(private val todayWatchedListDao: TodayWatchedListDao) : TodayWatchedListDataSource {
    override suspend fun insertWatchedItem(item: TodayWatchedListEntity) {
        todayWatchedListDao.insertWatchedItem(item)
    }

    override suspend fun insertAllWatchedItems(items: List<TodayWatchedListEntity>) {
        todayWatchedListDao.insertAllWatchedItems(items)
    }

    override fun getAllWatchedItems(): Flow<List<TodayWatchedListEntity>> {
        return todayWatchedListDao.getAllWatchedItems()
    }

    override suspend fun clearWatchedList() {
        todayWatchedListDao.clearWatchedList()
    }
}