package com.app.data.api

import com.app.data.model.PlacesDetailResponseDTO
import com.app.data.model.PlacesResponseDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 서버와 통신 할 API 리스트(TourAPI 관련)
 */
interface ApiInterface2 {
    // 지역기반(서울) 관광 정보 조회
    @GET("{serviceUrl}/areaBasedList2")
    suspend fun getPlacesList(
        @Path("serviceUrl") serviceUrl: String,         // 영어: EngService2, 한글: KorService2
        @Query("numOfRows") numOfRows: Int = 10,
        @Query("pageNo") pageNo: Int,                   // 페이지 번호
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "Seoullo",
        @Query("serviceKey") serviceKey: String,        // API KEY
        @Query("_type") type: String = "JSON",
        @Query("arrange") arrange: String = "C",
        @Query("contentTypeId") contentTypeId: String,  // 컨텐츠 유형
        @Query("cat1") category1: String,               // 분류1
        @Query("cat2") category2: String,               // 분류2
        @Query("cat3") category3: String,               // 분류3
        @Query("areaCode") areaCode: String = "1"
    ): PlacesResponseDTO

    // 관광 상세 정보 조회
    @GET("{serviceUrl}/detailCommon2")
    suspend fun getPlacesInfoDetail(
        @Path("serviceUrl") serviceUrl: String,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "Seoullo",
        @Query("serviceKey") serviceKey: String,
        @Query("contentId") contentId: String,
        @Query("_type") type: String = "JSON"
    ): PlacesDetailResponseDTO
}