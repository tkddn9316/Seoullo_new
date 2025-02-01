package com.app.data.model

import com.google.gson.annotations.SerializedName

data class AutoCompleteResponseDTO(
    @SerializedName("suggestions")
    val suggestions: List<Suggestion>
) {
    data class Suggestion(
        @SerializedName("placePrediction")
        val placePrediction: PlacePrediction
    ) {
        data class PlacePrediction(
            @SerializedName("placeId")
            val placeId: String,
            @SerializedName("structuredFormat")
            val structuredFormat: StructuredFormat
        ) {
            data class StructuredFormat(
                @SerializedName("mainText")
                val mainText: MainText,
                @SerializedName("secondaryText")
                val secondaryText: SecondaryText
            ) {
                data class MainText(
                    @SerializedName("text")
                    val text: String
                )

                data class SecondaryText(
                    @SerializedName("text")
                    val text: String
                )
            }
        }
    }
}