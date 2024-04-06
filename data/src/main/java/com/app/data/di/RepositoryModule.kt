package com.app.data.di

import com.app.data.source.PlacesNearbyDataSource
import com.app.data.source.UserDataSource
import com.app.data.repository.PlacesNearbyRepositoryImpl
import com.app.data.repository.UserRepositoryImpl
import com.app.domain.repository.PlacesNearbyRepository
import com.app.domain.repository.UserRepository
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
    fun providePlacesNearbyRepository(placesNearbyDataSource: PlacesNearbyDataSource): PlacesNearbyRepository {
        return PlacesNearbyRepositoryImpl(placesNearbyDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDataSource: UserDataSource): UserRepository {
        return UserRepositoryImpl(userDataSource)
    }
}