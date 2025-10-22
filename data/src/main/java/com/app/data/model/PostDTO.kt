package com.app.data.model

data class PostDTO(
    val title: String = "",
    val content: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorPhotoUrl: String = "",
    val createdAt: Long = 0L,
    val likeCount: Int = 0,
    val imageUrls: List<String> = emptyList()
)
