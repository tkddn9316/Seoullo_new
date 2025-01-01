package com.app.domain.model

import com.app.domain.model.common.BaseModel

data class ReverseGeocoding(
    val address: String = "",
    val placeId: String = ""
) : BaseModel()
