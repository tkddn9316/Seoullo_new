package com.app.data.di

import com.app.data.api.ApiInterface
import com.app.data.source.PlacesNearbyDataSource
import com.app.data.source.PlacesNearbyDataSourceImpl
import com.app.data.source.PlacesPhotoNearbyDataSource
import com.app.data.source.PlacesPhotoNearbyDataSourceImpl
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
    fun providePlacesNearbyDataSource(apiInterface: ApiInterface): PlacesNearbyDataSource {
        return PlacesNearbyDataSourceImpl(apiInterface)
    }

    @Provides
    @Singleton
    fun providePlacesPhotoNearbyDataSource(apiInterface: ApiInterface): PlacesPhotoNearbyDataSource {
        return PlacesPhotoNearbyDataSourceImpl(apiInterface)
    }
}