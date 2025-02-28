package com.app.domain.model

import com.app.domain.model.common.BaseModel

data class TodayWatchedList(
    val index: Int = 0,
    val isNearby: String,
    val name: String = "",
    val id: String = "",
    val contentTypeId: String = "",
    val displayName: String = "",
    val address: String = "",
    val description: String = "",
    val openNow: Boolean = false,
    val weekdayDescriptions: List<String> = emptyList(),
    val rating: Double = 0.0,
    val userRatingCount: Int = 0,
    val photoUrl: String = ""
) : BaseModel()
