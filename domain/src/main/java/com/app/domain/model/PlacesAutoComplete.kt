package com.app.domain.model

import com.app.domain.model.common.BaseModel

data class PlacesAutoComplete(
    val items: List<Item> = emptyList()
) : BaseModel() {
    data class Item(
        val placeId: String,
        val displayName: String,
        val address: String
    )
}
