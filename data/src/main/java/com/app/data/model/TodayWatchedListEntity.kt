package com.app.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.app.data.db.Converters

@Entity(tableName = "today_watched_list", indices = [Index(value = ["id"], unique = true)])
@TypeConverters(Converters::class)
data class TodayWatchedListEntity(
    @PrimaryKey(autoGenerate = true)
    val index: Int = 0,
    val isNearby: String,
    val name: String = "",
    @ColumnInfo(name = "id")
    val id: String = "",
    val contentTypeId: String = "",
    val displayName: String = "",
    val address: String = "",
    val description: String = "",
    val openNow: Boolean = false,
    val weekdayDescriptions: List<String> = emptyList(),
    val rating: Double = 0.0,
    val userRatingCount: Int = 0,
    val photoUrl: String = ""
)
