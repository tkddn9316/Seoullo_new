package com.app.domain.model

/**
 * 실제로 사용되는 TourInfo Model.
 * Data 계층에서 가져온 TourInfoDTO가 Mapper로 인하여 TourInfo로 변경되었다.
 */
data class TourInfo(
    val addr1: String,
    val addr2: String,
    val areacode: String,
    val contentid: String,
    val contenttypeid: String,
    val dist: String,
    val firstimage: String,
    val firstimage2: String,
    val mapx: String,
    val mapy: String,
    val mlevel: String,
    val modifiedtime: String,
    val sigungucode: String,
    val tel: String,
    val title: String
)
