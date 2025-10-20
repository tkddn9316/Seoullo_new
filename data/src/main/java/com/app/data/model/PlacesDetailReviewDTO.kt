package com.app.data.model

data class PlacesDetailReviewDTO(
    val text: String = "",
    val category: String = "",
    val contentId: String = "",
    val contentTypeId: String = "",
    val profileName: String = "",
    val profilePhotoUrl: String = "",
    val rating: Int = 0,
    val timestamp: Long = 0L
)
