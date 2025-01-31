package com.app.seoullo_new.module

import android.content.Context
import com.app.seoullo_new.view.util.WeatherUIRepository
import com.app.domain.repository.MapRepository
import com.app.seoullo_new.utils.CheckingManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {
    @Provides
    @Singleton
    fun provideCheckingManager(@ApplicationContext context: Context): CheckingManager {
        return CheckingManager(context)
    }

    @Provides
    @Singleton
    fun provideMapRepository(): MapRepository {
        return MapRepository()
    }

    @Provides
    @Singleton
    fun provideWeatherUIRepository(): WeatherUIRepository {
        return WeatherUIRepository()
    }
}