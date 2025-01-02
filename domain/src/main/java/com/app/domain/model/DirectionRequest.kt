package com.app.domain.model

import com.app.domain.model.common.BaseModel
import kotlinx.serialization.Serializable

@Serializable
data class DirectionRequest(
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val address: String = "",
    val placeId: String = ""    // Google 전용!
) : BaseModel()

// request 우선사항 순서: placeId -> lat/lng -> address
