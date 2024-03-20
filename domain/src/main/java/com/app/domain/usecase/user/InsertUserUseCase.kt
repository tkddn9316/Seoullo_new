package com.app.domain.usecase.user

import com.app.domain.model.User
import com.app.domain.repository.UserRepository
import javax.inject.Inject

class InsertUserUseCase @Inject constructor(private val repository: UserRepository) {

    operator fun invoke(user: User) = repository.insert(user)
}