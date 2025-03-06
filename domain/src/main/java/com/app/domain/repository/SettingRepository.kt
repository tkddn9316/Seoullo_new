package com.app.domain.repository

import com.app.domain.model.theme.ThemeSetting
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    suspend fun fetchThemes(): ThemeSetting
    suspend fun updateThemes(themeSetting: ThemeSetting)
    suspend fun updateLanguage(themeSetting: ThemeSetting)

    fun getHideTodayWatchedList(): Flow<Boolean>
    suspend fun updateHideTodayWatchedList(hideTodayWatchedList: Boolean)
}