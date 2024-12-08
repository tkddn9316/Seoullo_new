package com.app.data.model

import com.google.gson.annotations.SerializedName

data class PlacesDetailGoogleResponseDTO(
    @SerializedName("location")
    val location: Location,
    @SerializedName("reviews")
    val reviews: List<Review>?,
    @SerializedName("googleMapsLinks")
    val googleMapsLinks: GoogleMapsLinks,
    @SerializedName("nationalPhoneNumber")
    val nationalPhoneNumber: String?,
) {
    data class Location(
        @SerializedName("latitude")
        val latitude: Double,
        @SerializedName("longitude")
        val longitude: Double
    )

    data class Review(
        @SerializedName("relativePublishTimeDescription")
        val relativePublishTimeDescription: String,
        @SerializedName("rating")
        val rating: Int,
        @SerializedName("text")
        val text: Text?,
        @SerializedName("authorAttribution")
        val authorAttribution: AuthorAttribution
    )

    data class Text(
        @SerializedName("text")
        val text: String?
    )

    data class AuthorAttribution(
        @SerializedName("displayName")
        val profileName: String,
        @SerializedName("photoUri")
        val profilePhotoUrl: String
    )

    data class GoogleMapsLinks(
        @SerializedName("reviewsUri")
        val reviewsUri: String
    )
}
