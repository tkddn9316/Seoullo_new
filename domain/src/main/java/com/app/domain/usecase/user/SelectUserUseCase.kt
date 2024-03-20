package com.app.domain.usecase.user

import com.app.domain.model.User
import com.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SelectUserUseCase @Inject constructor(private val repository: UserRepository) {

    operator fun invoke(): Flow<List<User>> = repository.getAll()
}