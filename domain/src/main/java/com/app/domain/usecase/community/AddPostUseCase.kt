package com.app.domain.usecase.community

import com.app.domain.model.User
import com.app.domain.repository.BoardRepository
import javax.inject.Inject

class AddPostUseCase @Inject constructor(private val boardRepository: BoardRepository) {
    suspend operator fun invoke(
        user: User,
        title: String,
        content: String,
        images: List<ByteArray>
    ): String {
        require(title.isNotEmpty()) { "제목을 입력하세요." }
        require(content.isNotEmpty()) { "내용을 입력하세요." }
        return boardRepository.addPost(
            user = user,
            title = title,
            content = content,
            images = images
        )
    }
}