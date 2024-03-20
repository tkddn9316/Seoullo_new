package com.app.seoullo_new.module

import android.content.Context
import com.app.seoullo_new.utils.CheckingManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideCheckingManager(@ApplicationContext context: Context): CheckingManager {
        return CheckingManager(context)
    }
}