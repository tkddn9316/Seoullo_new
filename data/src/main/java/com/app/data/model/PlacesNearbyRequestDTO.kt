package com.app.data.model

import com.google.gson.annotations.SerializedName

data class PlacesNearbyRequestDTO(
    @SerializedName("includedTypes")
    var includedTypes: List<String>,
    @SerializedName("maxResultCount")
    var maxResultCount: Int,
    @SerializedName("languageCode")
    var languageCode: String,
    @SerializedName("locationRestriction")
    var locationRestriction: LocationRestriction
) {
    data class LocationRestriction(
        @SerializedName("circle")
        var circle: Circle
    )

    data class Circle(
        @SerializedName("center")
        var center: Center,
        @SerializedName("radius")
        var radius: Double
    )

    data class Center(
        @SerializedName("latitude")
        var latitude: String,
        @SerializedName("longitude")
        var longitude: String
    )
}
