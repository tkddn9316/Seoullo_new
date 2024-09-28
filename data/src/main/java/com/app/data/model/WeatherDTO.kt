package com.app.data.model

data class WeatherDTO(
    val response: Response
) {
    data class Response(
        val header: Header,
        val body: Body
    )

    data class Header(
        val resultCode: Int,
        val resultMsg: String
    )

    data class Body(
        val dataType: String,
        val items: Items
    )

    data class Items(
        val item: List<Item>
    )

    data class Item(
        val baseData: Int,
        val baseTime: Int,
        val category: String,
        val fcstDate : Int,
        val fcstTime : Int,
        val fcstValue : String,
        val nx : Int,
        val ny : Int
    )
}

//- category
//POP	강수확률
//PTY	강수형태(없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4))
//PCP	1시간 강수량
//REH	습도
//SNO	1시간 신적설
//SKY	하늘상태(맑음(1), 구름많음(3), 흐림(4))
//TMP	1시간 기온
//TMN	일 최저기온
//TMX	일 최고기온
//UUU	풍속(동서성분)
//VVV	풍속(남북성분)
//WAV	파고
//VEC	풍향
//WSD	풍속
