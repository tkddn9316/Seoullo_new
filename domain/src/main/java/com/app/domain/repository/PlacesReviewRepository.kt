package com.app.domain.repository

import com.app.domain.model.PlacesDetailReview
import kotlinx.coroutines.flow.Flow

interface PlacesReviewRepository {
    fun getDocumentPath(contentName: String): String

    fun getReviews(contentName: String): Flow<List<PlacesDetailReview>>
}