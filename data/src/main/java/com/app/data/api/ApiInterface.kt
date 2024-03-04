package com.app.data.api

import com.app.data.model.TourInfoResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 서버와 통신 할 API 리스트
 */
interface ApiInterface {
    // 위치기반 관광 정보 조회
    @GET("locationBasedList1")
    suspend fun getTourInfoList(
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "Seoullo",
        @Query("_type") type: String = "JSON",
        @Query("listYN") listYN: String = "Y",
        @Query("serviceKey") serviceKey: String,
        @Query("contentTypeId") contentTypeId: String,
        @Query("mapX") mapX: String,
        @Query("mapY") mapY: String,
        @Query("radius") radius: Int     // m 단위
    ): TourInfoResponse

    // 관광 상세 정보 조회
    @GET("detailCommon1")
    suspend fun getTourInfoDetail(
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "Seoullo",
        @Query("serviceKey") serviceKey: String,
        @Query("contentId") contentId: String,
        @Query("contentTypeId") contentTypeId: String,
    )
}