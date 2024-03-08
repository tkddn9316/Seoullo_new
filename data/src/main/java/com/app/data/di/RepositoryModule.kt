package com.app.data.di

import com.app.data.data_source.TourInfoDataSource
import com.app.data.data_source.UserDataSource
import com.app.data.repository.TourInfoRepositoryImpl
import com.app.data.repository.UserRepositoryImpl
import com.app.domain.repository.TourInfoRepository
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
    fun provideTourInfoRepository(tourInfoDataSource: TourInfoDataSource): TourInfoRepository {
        return TourInfoRepositoryImpl(tourInfoDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDataSource: UserDataSource): UserRepository {
        return UserRepositoryImpl(userDataSource)
    }
}