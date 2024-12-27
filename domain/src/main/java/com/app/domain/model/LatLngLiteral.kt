package com.app.domain.model

import com.app.domain.model.common.BaseModel
import kotlinx.serialization.Serializable

@Serializable
data class LatLngLiteral(
    val lat: Double = 0.0,
    val lng: Double = 0.0
) : BaseModel()
