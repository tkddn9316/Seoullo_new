package com.app.seoullo_new.module

import android.content.Context
import com.app.domain.repository.HomeScreenRepository
import com.app.domain.repository.MapRepository
import com.app.seoullo_new.utils.CheckingManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class PresentationModule {
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
    fun provideHomeScreenRepository(): HomeScreenRepository {
        return HomeScreenRepository()
    }
}