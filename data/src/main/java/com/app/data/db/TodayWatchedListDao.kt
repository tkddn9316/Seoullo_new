package com.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.data.model.TodayWatchedListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodayWatchedListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchedItem(item: TodayWatchedListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWatchedItems(items: List<TodayWatchedListEntity>)

    @Query("SELECT * FROM today_watched_list ORDER BY `index` DESC")
    fun getAllWatchedItems(): Flow<List<TodayWatchedListEntity>>

    @Query("DELETE FROM today_watched_list")
    suspend fun clearWatchedList()
}