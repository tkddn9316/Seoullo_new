package com.app.domain.usecase.user

import com.app.domain.repository.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(private val repository: UserRepository) {
    operator fun invoke() = repository.deleteAll()
}