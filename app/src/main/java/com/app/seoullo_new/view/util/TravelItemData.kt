package com.app.seoullo_new.view.util

import kotlinx.serialization.Serializable

@Serializable
data class TravelItemData(
    val restaurant: List<TravelJsonItemData>? = null,
    val aaaaaaaaaa: List<TravelJsonItemData>? = null
)

@Serializable
data class TravelJsonItemData(
    val name: String,           // 이름
    val color: Int,
    val id: String,
    val cat: String,       // 카테고리 코드
    val type: String     // 타입 (구분자로 여러 값 포함)
)
