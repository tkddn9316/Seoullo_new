package com.app.domain.repository

import com.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun insert(data: User)
    fun getAll(): Flow<List<User>>
    fun update(data: User)
    fun deleteAll()
}