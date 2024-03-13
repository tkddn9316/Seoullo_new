package com.app.data.di

import com.app.data.api.ApiInterface
import com.app.data.source.TourInfoDataSource
import com.app.data.source.TourInfoDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteDataModule {

    @Provides
    @Singleton
    fun provideTourInfoDataSource(apiInterface: ApiInterface): TourInfoDataSource {
        return TourInfoDataSourceImpl(apiInterface)
    }
}