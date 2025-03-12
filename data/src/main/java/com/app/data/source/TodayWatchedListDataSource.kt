package com.app.data.source

import com.app.data.model.TodayWatchedListEntity
import kotlinx.coroutines.flow.Flow

interface TodayWatchedListDataSource {
    suspend fun insertWatchedItem(item: TodayWatchedListEntity)
    suspend fun insertAllWatchedItems(items: List<TodayWatchedListEntity>)
    fun getAllWatchedItems(): Flow<List<TodayWatchedListEntity>>
    suspend fun clearWatchedList()
}