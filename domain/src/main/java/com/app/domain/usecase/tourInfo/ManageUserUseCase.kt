package com.app.domain.usecase.tourInfo

import com.app.domain.model.User
import com.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ManageUserUseCase @Inject constructor(private val repository: UserRepository) {

    fun insertUser(user: User) {
        repository.insert(user)
    }

    fun selectAllUser(): Flow<List<User>> = repository.getAll()

    fun deleteAllUser() {
        repository.deleteAll()
    }
}