package com.app.data.mapper

import com.app.data.model.CommentDTO
import com.app.data.model.PostDTO
import com.app.domain.model.community.Comment
import com.app.domain.model.community.Post
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toPost(): Post? =
    toObject(PostDTO::class.java)?.let { dto ->
        Post(
            id = id,
            title = dto.title,
            content = dto.content,
            authorId = dto.authorId,
            authorName = dto.authorName,
            authorPhotoUrl = dto.authorPhotoUrl,
            createdAt = dto.createdAt,
            likeCount = dto.likeCount,
            imageUrls = dto.imageUrls
        )
    }

fun DocumentSnapshot.toComment(): Comment? =
    toObject(CommentDTO::class.java)?.let { dto ->
        Comment(
            id = id,
            text = dto.text,
            authorId = dto.authorId,
            authorName = dto.authorName,
            authorPhotoUrl = dto.authorPhotoUrl,
            createdAt = dto.createdAt
        )
    }

fun Post.toDto() = PostDTO(
    title = title,
    content = content,
    authorId = authorId,
    authorName = authorName,
    authorPhotoUrl = authorPhotoUrl,
    createdAt = createdAt,
    likeCount = likeCount,
    imageUrls = imageUrls
)

fun Comment.toDto() = CommentDTO(
    text = text,
    authorId = authorId,
    authorName = authorName,
    authorPhotoUrl = authorPhotoUrl,
    createdAt = createdAt
)