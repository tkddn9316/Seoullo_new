package com.app.domain.repository

import com.app.domain.model.PlacesDetailReview
import com.app.domain.model.common.ApiState
import kotlinx.coroutines.flow.Flow

interface PlacesReviewRepository {
    fun getDocumentPath(contentName: String): String

    fun getReviews(contentName: String): Flow<List<PlacesDetailReview>>

    suspend fun addReview(contentName: String, review: PlacesDetailReview): Flow<ApiState<Unit>>
}