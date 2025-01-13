package com.app.data.model

import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    @SerializedName("response")
    val response: Response
) {
    data class Response(
        @SerializedName("header")
        val header: Header,
        @SerializedName("body")
        val body: Body? = null
    )

    data class Header(
        @SerializedName("resultCode")
        val resultCode: String,
        @SerializedName("resultMsg")
        val resultMsg: String
    )

    data class Body(
        @SerializedName("dataType")
        val dataType: String? = null,
        @SerializedName("items")
        val items: Items? = null,
    )

    data class Items(
        @SerializedName("item")
        val item: List<Item>
    )

    data class Item(
        @SerializedName("baseData")
        val baseData: Int,
        @SerializedName("baseTime")
        val baseTime: Int,
        @SerializedName("category")
        val category: String,
        @SerializedName("fcstDate")
        val fcstDate : Int,
        @SerializedName("fcstTime")
        val fcstTime : Int,
        @SerializedName("fcstValue")
        val fcstValue : String,
        @SerializedName("nx")
        val nx : Int,
        @SerializedName("ny")
        val ny : Int
    )
}

//- category
//POP	강수확률
//PTY	강수형태(없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)) icon: 1(SKY)/0(PTY), 3/0, 4/0 일 때는? 각각 해/구름/구름 아이콘
//PCP	1시간 강수량
//REH	습도
//SNO	1시간 신적설
//SKY	하늘상태(맑음(1), 구름많음(3), 흐림(4)) background
//TMP	1시간 기온
//TMN	일 최저기온
//TMX	일 최고기온
//UUU	풍속(동서성분)
//VVV	풍속(남북성분)
//WAV	파고
//VEC	풍향
//WSD	풍속
