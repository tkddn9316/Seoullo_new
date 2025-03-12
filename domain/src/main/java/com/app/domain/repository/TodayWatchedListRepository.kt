package com.app.domain.repository

import com.app.domain.model.TodayWatchedList
import kotlinx.coroutines.flow.Flow

interface TodayWatchedListRepository {
    suspend fun insertWatchedItem(item: TodayWatchedList)
    suspend fun insertAllWatchedItems(items: List<TodayWatchedList>)
    fun getAllWatchedItems(): Flow<List<TodayWatchedList>>
    suspend fun clearWatchedList()
    suspend fun onCancelWatchedList()
}