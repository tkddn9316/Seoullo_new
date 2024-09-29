package com.app.data.di

import com.app.data.api.ApiAnnotation
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
    @ApiAnnotation.GoogleApi
    @Provides
    @Singleton
    fun provideApiInterfaceGoogle(): ApiInterface {
        return ApiClient.createGoogleApi()
    }

    @ApiAnnotation.TourApi
    @Provides
    @Singleton
    fun provideApiInterfaceTourApi(): ApiInterface2 {
        return ApiClient.createSeoulTourApi()
    }

    @ApiAnnotation.OpenWeatherApi
    @Provides
    @Singleton
    fun provideApiInterfaceOpenWeatherApi(): ApiInterface3 {
        return ApiClient.createOpenWeatherApi()
    }
}


// 모듈 클래스 명은 XXXModule,
// 객체를 제공할 메서드 명은 provideXXX() 형태로 짓기