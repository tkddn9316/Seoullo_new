package com.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.data.model.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SeoulloDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}