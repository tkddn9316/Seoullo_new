package com.app.domain.model

data class PlacesRequest(
    var includedTypes: List<String>,
    var maxResultCount: Int,
    var locationRestriction: LocationRestriction
) : BaseModel() {
    data class LocationRestriction(
        var circle: Circle
    )

    data class Circle(
        var center: Center,
        var radius: Double
    )

    data class Center(
        var latitude: String,
        var longitude: String
    )
}
