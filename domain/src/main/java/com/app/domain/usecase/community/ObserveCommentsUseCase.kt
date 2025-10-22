package com.app.domain.usecase.community

import com.app.domain.repository.BoardRepository
import javax.inject.Inject

class ObserveCommentsUseCase @Inject constructor(private val boardRepository: BoardRepository) {
    operator fun invoke(postId: String) = boardRepository.observeComments(postId = postId)
}