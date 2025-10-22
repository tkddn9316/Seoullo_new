package com.app.data.model

data class CommentDTO(
    val text: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorPhotoUrl: String = "",
    val createdAt: Long = 0L
)
