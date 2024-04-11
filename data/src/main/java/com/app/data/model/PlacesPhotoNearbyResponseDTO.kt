package com.app.data.model

import com.google.gson.annotations.SerializedName

/**
 * Google Places Photo API
 */
data class PlacesPhotoNearbyResponseDTO(
    @SerializedName("name")
    val name: String,
    @SerializedName("photoUri")
    val photoUri: String,
)
