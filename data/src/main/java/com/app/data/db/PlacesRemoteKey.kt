package com.app.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places_remote_keys")
data class PlacesRemoteKey(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "contentid") val contentId: String,
    val cat1: String,
    val cat2: String,
    val cat3: String,
    val key: Int?
)
