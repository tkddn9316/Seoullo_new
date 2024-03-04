package com.app.data.model

import com.google.gson.annotations.SerializedName

data class TourInfoDTO(
    @SerializedName("addr1")
    val addr1: String,  // 주소
    @SerializedName("addr2")
    val addr2: String,  // 상세 주소
    @SerializedName("areacode")
    val areacode: String,   // 지역 코드
    @SerializedName("cat1")
    val cat1: String,   // 대분류
    @SerializedName("cat2")
    val cat2: String,   // 중분류
    @SerializedName("cat3")
    val cat3: String,   // 소분류
    @SerializedName("contentid")
    val contentid: String,  // 컨텐츠 ID
    @SerializedName("contenttypeid")
    val contenttypeid: String,  // 컨텐츠 타입 ID
    @SerializedName("cpyrhtDivCd")
    val cpyrhtDivCd: String,    // 저작권 유형
    @SerializedName("createdtime")
    val createdtime: String,    // 등록일
    @SerializedName("dist")
    val dist: String,   // 거리(단위: m)
    @SerializedName("firstimage")
    val firstimage: String,     // 대표 이미지(원본)
    @SerializedName("firstimage2")
    val firstimage2: String,    // 대표 이미지(썸네일)
    @SerializedName("mapx")
    val mapx: String,   // GPS X좌표
    @SerializedName("mapy")
    val mapy: String,   // GPS Y좌표
    @SerializedName("mlevel")
    val mlevel: String, // MAP 레벨
    @SerializedName("modifiedtime")
    val modifiedtime: String,   // 수정일
    @SerializedName("sigungucode")
    val sigungucode: String,    // 시군구 코드
    @SerializedName("tel")
    val tel: String,    // 전화번호
    @SerializedName("title")
    val title: String   // 제목
)