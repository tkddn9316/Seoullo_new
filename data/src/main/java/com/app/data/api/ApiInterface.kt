package com.app.data.api

import com.app.data.model.PlacesRequestDTO
import com.app.data.model.PlacesResponseDTO
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * 서버와 통신 할 API 리스트
 */
interface ApiInterface {
    // 위치기반 관광 정보 조회
    @POST("v1/places:searchNearby")
    @Headers("Content-Type: application/json")
    suspend fun getPlacesList(
        @Header("X-Goog-Api-Key") apiKey: String,
        @Header("X-Goog-FieldMask") fieldMask: String = "places.id,places.displayName,places.name,places.photos",
        @Body placesRequestDTO: PlacesRequestDTO
    ): PlacesResponseDTO
}