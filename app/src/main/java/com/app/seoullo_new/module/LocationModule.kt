package com.app.seoullo_new.module

import android.content.Context
import com.app.seoullo_new.utils.LocationService
import com.app.seoullo_new.utils.LocationServiceInterface
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class LocationModule {

    @ViewModelScoped
    @Binds
    abstract fun bindLocationService(
        locationService: LocationService
    ): LocationServiceInterface

    companion object {

        @ViewModelScoped
        @Provides
        fun provideLocationService(
            @ApplicationContext context: Context
        ) = LocationService(
            context = context,
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        )
    }
}