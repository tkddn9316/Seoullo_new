package com.app.data.model

import com.google.gson.annotations.SerializedName

data class PlacesNearbyResponseDTO(
    @SerializedName("places")
    val place: List<Place>
) {
    data class Place(
        @SerializedName("name")
        val name: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("displayName")
        val displayName: DisplayName,
        @SerializedName("formattedAddress")
        val formattedAddress: String,
        @SerializedName("photos")
        val photos: List<Photos>?,
        @SerializedName("primaryTypeDisplayName")
        val primaryTypeDisplayName: PrimaryTypeDisplayName,
        @SerializedName("regularOpeningHours")
        val regularOpeningHours: RegularOpeningHours?,
        @SerializedName("rating")
        val rating: Double,
        @SerializedName("userRatingCount")
        val userRatingCount: Int,
        var photoUrl: String = ""
    )

    data class DisplayName(
        @SerializedName("text")
        val text: String,
        @SerializedName("languageCode")
        val languageCode: String
    )

    data class Photos(
        @SerializedName("name")
        val name: String,
        @SerializedName("widthPx")
        val widthPx: String,
        @SerializedName("heightPx")
        val heightPx: String
    )

    data class PrimaryTypeDisplayName(
        @SerializedName("text")
        val text: String,
        @SerializedName("languageCode")
        val languageCode: String
    )

    data class RegularOpeningHours(
        @SerializedName("openNow")
        val openNow: Boolean = false,
        @SerializedName("weekdayDescriptions")
        val weekdayDescriptions: List<String>
    )
}


//{
//  "places": [
//    {
//      "displayName": {
//        "text": "Tanduleu",
//        "languageCode": "en"
//      }
//    },
//    {
//      "displayName": {
//        "text": "잇쇼니라멘",
//        "languageCode": "en"
//      }
//    }
//  ]
//}
