package com.app.domain.model

data class PlacesAutoCompleteRequest(
    val input: String,
    val languageCode: String,
)
