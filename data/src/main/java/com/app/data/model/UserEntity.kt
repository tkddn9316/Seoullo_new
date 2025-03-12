package com.app.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user", indices = [Index(value = ["tokenId"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val index: Int,
    val name: String,
    val email: String,
    @ColumnInfo(name = "tokenId")
    val tokenId: String,
    val photoUrl: String
)
