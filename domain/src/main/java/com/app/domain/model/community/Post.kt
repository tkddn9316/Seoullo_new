package com.app.domain.model.community

data class Post(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorPhotoUrl: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val imageUrls: List<String> = emptyList()
)
