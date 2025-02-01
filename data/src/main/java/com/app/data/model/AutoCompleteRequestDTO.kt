package com.app.data.model

import com.google.gson.annotations.SerializedName

data class AutoCompleteRequestDTO(
    @SerializedName("input")
    val input: String,
    @SerializedName("languageCode")
    val languageCode: String,
    @SerializedName("includeQueryPredictions")
    val includeQueryPredictions: Boolean = true,    // true인 경우 응답에 장소 및 검색어 예측이 모두 포함됩니다.
    @SerializedName("includedRegionCodes")
    val includedRegionCodes: List<String> = listOf("kr")    // 한국 지역만 검색
)
