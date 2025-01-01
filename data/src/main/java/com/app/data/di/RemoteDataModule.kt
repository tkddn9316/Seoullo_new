package com.app.data.di

import com.app.data.api.ApiAnnotation
import com.app.data.api.ApiInterface
import com.app.data.api.ApiInterface2
import com.app.data.api.ApiInterface3
import com.app.data.source.DirectionDataSource
import com.app.data.source.DirectionDataSourceImpl
import com.app.data.source.PlacesDataSource
import com.app.data.source.PlacesDataSourceImpl
import com.app.data.source.PlacesDetailDataSource
import com.app.data.source.PlacesDetailDataSourceImpl
import com.app.data.source.PlacesDetailGoogleDataSource
import com.app.data.source.PlacesDetailGoogleDataSourceImpl
import com.app.data.source.PlacesNearbyDataSource
import com.app.data.source.PlacesNearbyDataSourceImpl
import com.app.data.source.PlacesPhotoNearbyDataSource
import com.app.data.source.PlacesPhotoNearbyDataSourceImpl
import com.app.data.source.ReverseGeocodingDataSource
import com.app.data.source.ReverseGeocodingDataSourceImpl
import com.app.data.source.WeatherDataSource
import com.app.data.source.WeatherDataSourceImpl
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
    fun providePlacesNearbyDataSource(@ApiAnnotation.GooglePlacesApi apiInterface: ApiInterface): PlacesNearbyDataSource {
        return PlacesNearbyDataSourceImpl(apiInterface)
    }

    @Provides
    @Singleton
    fun providePlacesPhotoNearbyDataSource(@ApiAnnotation.GooglePlacesApi apiInterface: ApiInterface): PlacesPhotoNearbyDataSource {
        return PlacesPhotoNearbyDataSourceImpl(apiInterface)
    }

    @Provides
    @Singleton
    fun providePlacesDetailGoogleDataSource(@ApiAnnotation.GooglePlacesApi apiInterface: ApiInterface): PlacesDetailGoogleDataSource {
        return PlacesDetailGoogleDataSourceImpl(apiInterface)
    }

    @Provides
    @Singleton
    fun provideReverseGeocodingDataSource(@ApiAnnotation.GoogleMapsApi apiInterface: ApiInterface): ReverseGeocodingDataSource {
        return ReverseGeocodingDataSourceImpl(apiInterface)
    }

    @Provides
    @Singleton
    fun provideDirectionDataSource(@ApiAnnotation.GoogleMapsApi apiInterface: ApiInterface): DirectionDataSource {
        return DirectionDataSourceImpl(apiInterface)
    }

    @Provides
    @Singleton
    fun providePlacesDataSource(@ApiAnnotation.TourApi apiInterface: ApiInterface2): PlacesDataSource {
        return PlacesDataSourceImpl(apiInterface)
    }

    @Provides
    @Singleton
    fun providePlacesDetailDataSource(@ApiAnnotation.TourApi apiInterface: ApiInterface2): PlacesDetailDataSource {
        return PlacesDetailDataSourceImpl(apiInterface)
    }

    @Provides
    @Singleton
    fun provideWeatherDataSource(@ApiAnnotation.OpenWeatherApi apiInterface: ApiInterface3): WeatherDataSource {
        return WeatherDataSourceImpl(apiInterface)
    }
}