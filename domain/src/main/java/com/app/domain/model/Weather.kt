package com.app.domain.model

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
