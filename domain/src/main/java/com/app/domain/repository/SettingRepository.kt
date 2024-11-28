package com.app.domain.repository

import com.app.domain.model.theme.ThemeSetting

interface SettingRepository {
    suspend fun fetchThemes(): ThemeSetting
//    suspend fun fetchLanguage(): ThemeSetting
    suspend fun updateThemes(themeSetting: ThemeSetting)
    suspend fun updateLanguage(themeSetting: ThemeSetting)
}