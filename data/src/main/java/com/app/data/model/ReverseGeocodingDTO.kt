package com.app.data.model

import com.google.gson.annotations.SerializedName

data class ReverseGeocodingDTO(
    @SerializedName("results")
    val results: List<Result>?,
    @SerializedName("status")
    val status: String
) {
    data class Result(
        @SerializedName("formatted_address")
        val address: String,
        @SerializedName("place_id")
        val placeId: String
    )
}