package com.app.data.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Api Module 에 사용할 API URL 선언
 */
object ApiClient {
    private const val BASE_URL_GOOGLE_MAPS = "https://maps.googleapis.com/"
    private const val BASE_URL_GOOGLE_PLACES = "https://places.googleapis.com/"
    private const val BASE_URL_TOUR_API = "https://apis.data.go.kr/B551011/"
    private const val BASE_URL_OPEN_WEATHER_MAP = "https://api.openweathermap.org/data/3.0/"
    private const val BASE_URL_SEOUL_OPEN_API = "http://openapi.seoul.go.kr:8088/"
    private const val BASE_URL_SEOUL_SUNRISE_API = "https://apis.data.go.kr/B090041/"
    private const val TIMEOUT = 15

    private val commonHttpClient: OkHttpClient by lazy {
        createHttpClient()
    }

    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .client(commonHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
    }

    fun createGooglePlacesApi(): ApiInterface {
        return retrofitBuilder
            .baseUrl(BASE_URL_GOOGLE_PLACES)
            .build()
            .create(ApiInterface::class.java)
    }

    fun createGoogleMapsApi(): ApiInterface {
        return retrofitBuilder
            .baseUrl(BASE_URL_GOOGLE_MAPS)
            .build()
            .create(ApiInterface::class.java)
    }

    fun createSeoulTourApi(): ApiInterface2 {
        return retrofitBuilder
            .baseUrl(BASE_URL_TOUR_API)
            .build()
            .create(ApiInterface2::class.java)
    }

    fun createOpenWeatherApi(): ApiInterface3 {
        return retrofitBuilder
            .baseUrl(BASE_URL_OPEN_WEATHER_MAP)
            .build()
            .create(ApiInterface3::class.java)
    }

    fun createSeoulOpenApi(): ApiInterface3 {
        return retrofitBuilder
            .baseUrl(BASE_URL_SEOUL_OPEN_API)
            .build()
            .create(ApiInterface3::class.java)
    }

    fun createSeoulSunriseApi(): ApiInterface3 {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_SEOUL_SUNRISE_API)
            .client(commonHttpClient)
            .build()
            .create(ApiInterface3::class.java)
    }

    private fun createHttpClient(): OkHttpClient {
        val interceptors = listOf(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        )
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
        interceptors.forEach { builder.addInterceptor(it) }

        return builder.build()
    }
}