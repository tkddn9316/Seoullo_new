package com.app.data.model

import com.google.gson.annotations.SerializedName

data class DustDTO(
    @SerializedName("ListAvgOfSeoulAirQualityService")
    val result: Result
) {
    data class Result(
        @SerializedName("RESULT")
        val resultCode: ResultCode,
        @SerializedName("list_total_count")
        val listTotalCount: Int,
        @SerializedName("row")
        val items: List<Item>
    ) {
        data class ResultCode(
            @SerializedName("CODE")
            val code: String,
            @SerializedName("MESSAGE")
            val message: String
        )

        data class Item(
            @SerializedName("CARBON")
            val carbon: Double,
            @SerializedName("GRADE")
            val grade: String,  // 통합대기환경지수
            @SerializedName("IDEX_MVL")
            val idxMVL: String,
            @SerializedName("NITROGEN")
            val nitrogen: Double,
            @SerializedName("OZONE")
            val ozone: Double,  // 일산화탄소(단위:ppm)
            @SerializedName("PM10")
            val fineDust: Int,  // 미세먼지
            @SerializedName("PM25")
            val ultraFineDust: Int,  // 초미세먼지
            @SerializedName("POLLUTANT")
            val pollutant: String,  // 이산화질소(단위:ppm)
            @SerializedName("SULFUROUS")
            val sulfurous: Double
        )
    }
}

//{
//  "ListAvgOfSeoulAirQualityService": {
//    "list_total_count": 1,
//    "RESULT": {
//      "CODE": "INFO-000",
//      "MESSAGE": "정상 처리되었습니다"
//    },
//    "row": [
//      {
//        "GRADE": "보통",
//        "IDEX_MVL": "87",
//        "POLLUTANT": "PM-2.5",
//        "NITROGEN": 0.032,
//        "OZONE": 0.027,
//        "CARBON": 0.6,
//        "SULFUROUS": 0.003,
//        "PM10": 43,
//        "PM25": 30
//      }
//    ]
//  }