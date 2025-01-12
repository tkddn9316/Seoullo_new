package com.app.data.api

import com.app.data.model.WeatherDTO
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 서버와 통신 할 API 리스트(기상 관련)
 */
interface ApiInterface3 {
    // 단기 예보 조회
    @GET("getVilageFcst")
    suspend fun getWeather(
        @Query("serviceKey") serviceKey: String,
        @Query("dataType") dataType : String = "JSON",
        @Query("numOfRows") numOfRows : Int = 12,
        @Query("pageNo") pageNo : Int = 1,
        @Query("base_date") baseDate : String,
        @Query("base_time") baseTime : String,
        @Query("nx") nx : String = "60",    // 서울 중구 기준
        @Query("ny") ny : String = "127"
    ): WeatherDTO
}