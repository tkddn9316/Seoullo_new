package com.app.domain.model

import com.app.domain.model.common.BaseModel

data class Weather(
    val baseData: Int,
    val baseTime: Int,
    val category: String,
    val fcstDate : Int,
    val fcstTime : Int,
    val fcstValue : String,
    val nx : Int,
    val ny : Int
) : BaseModel()
