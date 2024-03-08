package com.app.data.data_source

import com.app.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    fun insert(data: UserEntity)
    fun getAll(): Flow<List<UserEntity>>
    fun update(data: UserEntity)
    fun deleteAll()
}