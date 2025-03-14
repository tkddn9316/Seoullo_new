package com.app.domain.model

import com.app.domain.model.common.BaseModel
import kotlinx.serialization.Serializable

@Serializable
data class PlacesDetailReview(
    val category: String = "",
    val contentId: String = "",
    val contentTypeId: String = "",
    val profileName: String = "",
    val profilePhotoUrl: String = "",
    val rating: Int = 1,            // 별점 (1~5)
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
