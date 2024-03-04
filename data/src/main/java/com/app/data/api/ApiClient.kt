package com.app.data.api

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Api Module 에 사용할 API URL 선언
 */
object ApiClient {
    private const val BASE_URL = "https://apis.data.go.kr/B551011/EngService1/"
    private const val TIMEOUT = 15

    fun create(): ApiInterface {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .client(createHttpClient())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(ApiInterface::class.java)
    }

    private fun createHttpClient(): OkHttpClient {
        val interceptors = ArrayList<Interceptor>()
        return getOkHttpClient(interceptors)
    }

    private fun getOkHttpClient(interceptors: ArrayList<Interceptor>): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        interceptors.add(loggingInterceptor)

        val builder = getBuilder(0)
        for (interceptor in interceptors) {
            builder.addInterceptor(interceptor)
        }
        return builder.build()
    }

    private fun getBuilder(time: Int): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout((if (time > 0) time else TIMEOUT).toLong(), TimeUnit.SECONDS)
            .writeTimeout((if (time > 0) time else TIMEOUT).toLong(), TimeUnit.SECONDS)
            .readTimeout((if (time > 0) time else TIMEOUT).toLong(), TimeUnit.SECONDS)
    }
}