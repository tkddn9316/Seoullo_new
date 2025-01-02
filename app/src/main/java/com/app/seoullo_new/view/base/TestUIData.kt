package com.app.seoullo_new.view.base

import com.app.domain.model.theme.ThemeSetting
import com.app.domain.repository.SettingRepository
import com.app.seoullo_new.di.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MockDispatcherProvider : DispatcherProvider {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val default: CoroutineDispatcher = Dispatchers.Default
}

class MockSettingRepository : SettingRepository {
    override suspend fun fetchThemes(): ThemeSetting {
        return ThemeSetting()
    }

    override suspend fun updateThemes(themeSetting: ThemeSetting) {}

    override suspend fun updateLanguage(themeSetting: ThemeSetting) {}
}