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
