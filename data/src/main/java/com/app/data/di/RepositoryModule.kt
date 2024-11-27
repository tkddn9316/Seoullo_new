package com.app.data.di

import com.app.data.source.PlacesNearbyDataSource
import com.app.data.source.UserDataSource
import com.app.data.repository.PlacesNearbyRepositoryImpl
import com.app.data.repository.PlacesRepositoryImpl
import com.app.data.repository.SettingRepositoryImpl
import com.app.data.repository.UserRepositoryImpl
import com.app.data.repository.WeatherRepositoryImpl
import com.app.data.source.PlacesDataSource
import com.app.data.source.PlacesPhotoNearbyDataSource
import com.app.data.source.SettingDataSource
import com.app.data.source.WeatherDataSource
import com.app.domain.repository.PlacesNearbyRepository
import com.app.domain.repository.PlacesRepository
import com.app.domain.repository.SettingRepository
import com.app.domain.repository.UserRepository
import com.app.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt에게 해당 Repository를 제공
 */
@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun providePlacesNearbyRepository(placesNearbyDataSource: PlacesNearbyDataSource, placesPhotoNearbyDataSource: PlacesPhotoNearbyDataSource): PlacesNearbyRepository {
        return PlacesNearbyRepositoryImpl(placesNearbyDataSource, placesPhotoNearbyDataSource)
    }

    @Provides
    @Singleton
    fun providePlacesRepository(placesDataSource: PlacesDataSource): PlacesRepository {
        return PlacesRepositoryImpl(placesDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDataSource: UserDataSource): UserRepository {
        return UserRepositoryImpl(userDataSource)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(weatherDataSource: WeatherDataSource): WeatherRepository {
        return WeatherRepositoryImpl(weatherDataSource)
    }

    @Provides
    @Singleton
    fun provideSettingRepository(settingDataSource: SettingDataSource): SettingRepository {
        return SettingRepositoryImpl(settingDataSource)
    }
}