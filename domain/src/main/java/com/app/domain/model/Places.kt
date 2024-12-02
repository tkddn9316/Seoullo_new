package com.app.domain.model

/**
 * 실제로 사용되는 Place Model.
 * Data 계층에서 가져온 TourInfoResponseDTO가 Mapper로 인하여 Place로 변경되었다.
 */
data class Places(
    val name: String,
    val id: String,
    val displayName: String,
    val address: String,
    val description: String,
    val openNow: Boolean,
    val weekdayDescriptions: List<String>,
    val rating: Double,
    val userRatingCount: Int,
    val photoUrl: String
) : BaseModel()
