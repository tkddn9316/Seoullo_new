package com.app.data.api

import com.app.data.model.DirectionResponseDTO
import com.app.data.model.PlacesDetailGoogleResponseDTO
import com.app.data.model.PlacesNearbyRequestDTO
import com.app.data.model.PlacesNearbyResponseDTO
import com.app.data.model.PlacesPhotoNearbyResponseDTO
import com.app.data.model.ReverseGeocodingDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 서버와 통신 할 API 리스트(구글 관련)
 */
interface ApiInterface {
    // 위치기반 관광 정보 조회
    @POST("v1/places:searchNearby")
    @Headers("Content-Type: application/json")
    suspend fun getPlacesNearbyList(
        @Header("X-Goog-Api-Key") apiKey: String,
        @Header("X-Goog-FieldMask") fieldMask: String =
            "places.id,places.displayName,places.name,places.photos,places.formattedAddress,places.primaryTypeDisplayName,places.regularOpeningHours,places.rating,places.userRatingCount",
        @Body placesNearbyRequestDTO: PlacesNearbyRequestDTO
    ): PlacesNearbyResponseDTO

    // 사진 가져오기
    @GET("v1/{NAME}/media")
    suspend fun getPlacePhotoNearby(
        @Path(value = "NAME", encoded = true) name: String,
        @Query("maxHeightPx") maxHeightPx: Int = 600,
        @Query("maxWidthPx") maxWidthPx: Int = 900,
        @Query("key") key: String,
        @Query("skipHttpRedirect") skipHttpRedirect: Boolean = true     // JSON 응답값으로
    ): PlacesPhotoNearbyResponseDTO

    // 상세 정보
    @GET("v1/places/{placeId}")
    suspend fun getPlacesDetail(
        @Header("X-Goog-Api-Key") apiKey: String,
        @Header("X-Goog-FieldMask") fieldMask: String = "location,reviews,googleMapsLinks,nationalPhoneNumber",
        @Path("placeId") placeId: String,
        @Query("languageCode") languageCode: String
    ): PlacesDetailGoogleResponseDTO

    // 역 지오코딩(현재 위치의 주소 검색)
    @GET("maps/api/geocode/{outputFormat}")
    suspend fun getReverseGeocoding(
        @Path("outputFormat") outputFormat: String = "json",
        @Query("result_type") resultType: String = "street_address",
        @Query("latlng") latLng: String,    // 형태: "40.714224,-73.961452"
        @Query("language") languageCode: String,
        @Query("key") apiKey: String
    ): ReverseGeocodingDTO

    // 길 찾기
    @GET("maps/api/directions/{outputFormat}")
    suspend fun getDirection(
        @Path("outputFormat") outputFormat: String = "json",
        @Query("destination") destination: String,      // 위/경도, 주소 가능
        @Query("origin") starting: String,
        @Query("language") languageCode: String,
        @Query("mode") mode: String = "transit",
        @Query("transit_mode") transitMode: String = "bus|rail",
        @Query("transit_routing_preference") preference: String = "fewer_transfers",
        @Query("key") apiKey: String
    ): DirectionResponseDTO
}