package com.app.seoullo_new.module

import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.di.DispatcherProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Test 가능한 Dispatchers.
 * 새로운 코루틴을 만들 때 Dispatcher를 주입.
 */
@InstallIn(ViewModelComponent::class)
@Module
interface DispatcherModule {
    @Binds
    fun bindDispatcherProvider(provider: DispatcherProviderImpl): DispatcherProvider
}