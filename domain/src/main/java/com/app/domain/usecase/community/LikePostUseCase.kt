package com.app.domain.usecase.community

import com.app.domain.repository.BoardRepository
import javax.inject.Inject

class LikePostUseCase @Inject constructor(private val boardRepository: BoardRepository) {
    suspend operator fun invoke(postId: String, like: Boolean) =
        boardRepository.incrementLike(postId, if (like) +1 else -1)
}