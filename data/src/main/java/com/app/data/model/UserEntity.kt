package com.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val index: Int,
    var auto: String = "N",  // 자동 로그인 여부
    val name: String,
    val email: String,
    val photoUrl: String
)
