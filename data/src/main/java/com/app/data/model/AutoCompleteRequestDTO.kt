package com.app.data.model

import com.google.gson.annotations.SerializedName

data class AutoCompleteRequestDTO(
    @SerializedName("input")
    val input: String,
    @SerializedName("languageCode")
    val languageCode: String,
    @SerializedName("includeQueryPredictions")
    val includeQueryPredictions: Boolean = true
)
