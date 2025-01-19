package com.app.data.di

import com.app.data.api.ApiAnnotation.GoogleMapsApi
import com.app.data.api.ApiAnnotation.GooglePlacesApi
import com.app.data.api.ApiAnnotation.OpenWeatherApi
import com.app.data.api.ApiAnnotation.SeoulOpenApi
import com.app.data.api.ApiAnnotation.TourApi
import com.app.data.api.ApiClient
import com.app.data.api.ApiInterface
import com.app.data.api.ApiInterface2
import com.app.data.api.ApiInterface3
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    @GooglePlacesApi
    @Provides
    @Singleton
    fun provideApiInterfaceGooglePlaces(): ApiInterface {
        return ApiClient.createGooglePlacesApi()
    }

    @GoogleMapsApi
    @Provides
    @Singleton
    fun provideApiInterfaceGoogleMaps(): ApiInterface {
        return ApiClient.createGoogleMapsApi()
    }

    @TourApi
    @Provides
    @Singleton
    fun provideApiInterfaceTourApi(): ApiInterface2 {
        return ApiClient.createSeoulTourApi()
    }

    @OpenWeatherApi
    @Provides
    @Singleton
    fun provideApiInterfaceOpenWeatherApi(): ApiInterface3 {
        return ApiClient.createOpenWeatherApi()
    }

    @SeoulOpenApi
    @Provides
    @Singleton
    fun provideApiInterfaceSeoulOpenApi(): ApiInterface3 {
        return ApiClient.createSeoulOpenApi()
    }
}


// 모듈 클래스 명은 XXXModule,
// 객체를 제공할 메서드 명은 provideXXX() 형태로 짓기