package com.app.domain.model.community

data class Comment(
    val id: String = "",
    val text: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorPhotoUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
