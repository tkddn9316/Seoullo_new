package com.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.data.model.TodayWatchedListEntity
import com.app.data.model.UserEntity

@Database(
    entities = [UserEntity::class, TodayWatchedListEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SeoulloDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun todayWatchedListDao(): TodayWatchedListDao
}