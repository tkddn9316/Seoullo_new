package com.app.domain.usecase.community

import com.app.domain.model.User
import com.app.domain.repository.BoardRepository
import javax.inject.Inject

class LikePostUseCase @Inject constructor(private val boardRepository: BoardRepository) {
    //    suspend fun hasLiked(postId: String, user: User) =
//        boardRepository.hasLiked(postId = postId, user = user)
    suspend fun hasLiked(postId: String, user: User): Result<Boolean> = runCatching {
        boardRepository.hasLiked(postId = postId, user = user)
    }

    suspend fun like(postId: String, user: User): Result<Unit> = runCatching {
        boardRepository.like(postId = postId, user = user)
    }

    suspend fun unlike(postId: String, user: User): Result<Unit> = runCatching {
        boardRepository.unlike(postId = postId, user = user)
    }
}