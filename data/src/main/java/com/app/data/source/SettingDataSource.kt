package com.app.data.source

import com.app.domain.model.theme.DynamicTheme
import com.app.domain.model.theme.Language
import com.app.domain.model.theme.ThemeMode

interface SettingDataSource {
    suspend fun updateDynamicTheme(theme: DynamicTheme)
    suspend fun updateThemeMode(themeMode: ThemeMode)
    suspend fun updateLanguage(language: Language)

    suspend fun getDynamicTheme(): DynamicTheme?
    suspend fun getThemeMode(): ThemeMode?
    suspend fun getLanguage(): Language?
}