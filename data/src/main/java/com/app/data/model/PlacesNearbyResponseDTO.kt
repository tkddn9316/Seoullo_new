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
        val displayName: DisplayName
    )

    data class DisplayName(
        @SerializedName("text")
        val text: String,
        @SerializedName("languageCode")
        val languageCode: String
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
