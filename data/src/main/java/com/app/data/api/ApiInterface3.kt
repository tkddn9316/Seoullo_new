package com.app.data.api

import com.app.data.model.DustDTO
import com.app.data.model.SunriseDTO
import com.app.data.model.WeatherDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 서버와 통신 할 API 리스트(기상 관련)
 */
interface ApiInterface3 {
    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") lat: String = "37.567022",
        @Query("lon") lon: String = "126.978640",      // 서울시청 위도/경도 고정
        @Query("appid") apiKey: String,
        @Query("exclude") exclude: String = "minutely,hourly,alerts",    // 분 단위 예보, 시간별 예보 제외
        @Query("units") units: String = "metric",   // 단위: 섭씨
        @Query("lang") languageCode: String = "en"
    ): WeatherDTO

    @GET("{KEY}/{TYPE}/{SERVICE}/{START_INDEX}/{END_INDEX}")
    suspend fun getDustData(
        @Path("KEY") apiKey: String,
        @Path("TYPE") type: String = "json",
        @Path("SERVICE") service: String = "ListAvgOfSeoulAirQualityService",
        @Path("START_INDEX") startIndex: Int = 1,
        @Path("END_INDEX") endIndex: Int = 5
    ): DustDTO

    @GET("openapi/service/RiseSetInfoService/getAreaRiseSetInfo")
    suspend fun getSunriseTime(
        @Query("locdate") currentDate: String,
        @Query("location") location: String = "서울",
        @Query("ServiceKey") apiKey: String
    ): SunriseDTO
}