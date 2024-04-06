package com.app.data.api

import com.app.data.model.PlacesNearbyRequestDTO
import com.app.data.model.PlacesNearbyResponseDTO
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * 서버와 통신 할 API 리스트(구글 관련)
 */
interface ApiInterface {
    // 위치기반 관광 정보 조회
    @POST("v1/places:searchNearby")
    @Headers("Content-Type: application/json")
    suspend fun getPlacesNearbyList(
        @Header("X-Goog-Api-Key") apiKey: String,
        @Header("X-Goog-FieldMask") fieldMask: String = "places.id,places.displayName,places.name,places.photos",
        @Body placesNearbyRequestDTO: PlacesNearbyRequestDTO
    ): PlacesNearbyResponseDTO
}