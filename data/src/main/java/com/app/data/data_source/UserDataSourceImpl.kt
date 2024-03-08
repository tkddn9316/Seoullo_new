package com.app.data.data_source

import com.app.data.db.UserDao
import com.app.data.model.UserEntity
import com.app.data.utils.Logging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(private val userDao: UserDao) : UserDataSource {

    override fun insert(data: UserEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.insert(data)
        }
    }

    override fun getAll(): Flow<List<UserEntity>> = userDao.getAll()

    override fun update(data: UserEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.update(data)
        }
    }

    override fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.deleteAll()
        }
    }
}