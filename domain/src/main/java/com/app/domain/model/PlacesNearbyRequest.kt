package com.app.domain.model

import com.app.domain.model.common.BaseModel

data class PlacesNearbyRequest(
    var includedTypes: List<String>,
    var maxResultCount: Int,
    var languageCode: String,
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
