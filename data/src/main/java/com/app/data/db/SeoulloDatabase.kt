package com.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.data.db.entity.RestaurantEntity
import com.app.data.model.UserEntity

@Database(
    entities = [RestaurantEntity::class, UserEntity::class, PlacesRemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class SeoulloDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun restaurantListDao(): RestaurantListDao
    abstract fun placesRemoteKeyDao(): PlacesRemoteKeyDao
}