package com.app.domain.usecase.community

import com.app.domain.repository.BoardRepository
import javax.inject.Inject

class ObservePostsUseCase @Inject constructor(private val boardRepository: BoardRepository) {
    operator fun invoke() = boardRepository.observePosts()
}