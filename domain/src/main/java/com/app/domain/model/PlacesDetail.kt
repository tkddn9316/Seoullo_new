package com.app.domain.model

data class PlacesDetail(
    val contentId: String = "",
    val contentTypeId: String = "",
    val address: String = "",
    val photoUrl: String = "",
    val latitude: Double = 0.0,     // map y
    val longitude: Double = 0.0,    // map x
    val displayName: String = "",
    val description: String = "",
    val phoneNum: String = "",
    val homepage: String = ""
)
