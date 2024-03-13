package com.app.data.repository

import com.app.data.source.UserDataSource
import com.app.data.mapper.mapperToUser
import com.app.data.model.UserEntity
import com.app.domain.model.User
import com.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDataSource: UserDataSource) :
    UserRepository {
    override fun insert(data: User) {
        val userEntity = UserEntity(
            data.index, data.auto, data.name, data.email, data.photoUrl
        )
        userDataSource.insert(userEntity)
    }

    override fun getAll(): Flow<List<User>> {
        return flow {
            userDataSource.getAll().collect {
                emit(mapperToUser(it))
            }
        }
    }

    override fun update(data: User) {
        val userEntity = UserEntity(
            data.index, data.auto, data.name, data.email, data.photoUrl
        )
        userDataSource.update(userEntity)
    }

    override fun deleteAll() {
        userDataSource.deleteAll()
    }
}