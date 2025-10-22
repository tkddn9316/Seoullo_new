package com.app.domain.usecase.community

import com.app.domain.model.User
import com.app.domain.repository.BoardRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(private val boardRepository: BoardRepository) {
    suspend operator fun invoke(postId: String, user: User, text: String): String {
        require(text.isNotBlank()) { "댓글을 입력하세요." }
        return boardRepository.addComment(
            postId = postId,
            user = user,
            text = text
        )
    }
}