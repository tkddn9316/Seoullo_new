package com.app.data.di

import com.app.data.api.ApiInterface
import com.app.data.source.PlacesDataSource
import com.app.data.source.PlacesDataSourceImpl
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
    fun providePlacesDataSource(apiInterface: ApiInterface): PlacesDataSource {
        return PlacesDataSourceImpl(apiInterface)
    }
}