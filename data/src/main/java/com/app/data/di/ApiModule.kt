package com.app.data.di

import com.app.data.api.ApiClient
import com.app.data.api.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideApiInterface(): ApiInterface {
        return ApiClient.create()
    }
}


// 모듈 클래스 명은 XXXModule,
// 객체를 제공할 메서드 명은 provideXXX() 형태로 짓기