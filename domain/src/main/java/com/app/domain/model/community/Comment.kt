package com.app.domain.model.community

data class Comment(
    val id: String = "",
    val text: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorPhotoUrl: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isMine: Boolean = false,
    val replyList: List<Reply> = emptyList()
) {
    data class Reply(
        val id: String = "",
        val text: String = "",
        val authorId: String = "",
        val authorName: String = "",
        val authorPhotoUrl: String = "",
        val createdAt: Long = System.currentTimeMillis(),
        val isMine: Boolean = false
    )
}
