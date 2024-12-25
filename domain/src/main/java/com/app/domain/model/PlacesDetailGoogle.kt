package com.app.domain.model

import com.app.domain.model.common.BaseModel
import kotlinx.serialization.Serializable

@Serializable
data class PlacesDetailGoogle(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val reviews: List<Review> = emptyList(),
    val reviewsUri: String = "",
    val phoneNumber: String = ""
) : BaseModel() {
    @Serializable
    data class Review(
        val profileName: String = "",
        val profilePhotoUrl: String = "",
        val relativePublishTimeDescription: String = "",
        val rating: Int = 0,
        val text: String = ""
    )
}
