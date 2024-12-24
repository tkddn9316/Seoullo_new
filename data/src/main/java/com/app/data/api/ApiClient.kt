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
    private const val BASE_URL_GOOGLE = "https://places.googleapis.com/"
    private const val BASE_URL_TOUR_API = "https://apis.data.go.kr/B551011/"
    private const val BASE_URL_OPEN_WEATHER = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"
    private const val TIMEOUT = 15

    private val commonHttpClient: OkHttpClient by lazy {
        createHttpClient()
    }

    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .client(commonHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
    }

    fun createGoogleApi(): ApiInterface {
        return retrofitBuilder
            .baseUrl(BASE_URL_GOOGLE)
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
            .baseUrl(BASE_URL_OPEN_WEATHER)
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