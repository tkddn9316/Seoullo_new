package com.app.domain.model

import kotlinx.serialization.Serializable

/**
 * 실제로 사용되는 Place Model.
 * Data 계층에서 가져온 TourInfoResponseDTO가 Mapper로 인하여 Place로 변경되었다.
 */
@Serializable
data class Places(
    val name: String = "",
    val id: String = "",
    val displayName: String = "",
    val address: String = "",
    val description: String = "",
    val openNow: Boolean = false,
    val weekdayDescriptions: List<String> = emptyList(),
    val rating: Double = 0.0,
    val userRatingCount: Int = 0,
    val photoUrl: String = ""
) : BaseModel()
