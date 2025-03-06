package com.app.data.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.app.domain.model.theme.DynamicTheme
import com.app.domain.model.theme.Language
import com.app.domain.model.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingDataSource {
    private val dynamicThemeKey = intPreferencesKey("dynamic_mode")
    private val themeModeKey = intPreferencesKey("theme_mode")
    private val languageKey = intPreferencesKey("setting_language")
    private val hideTodayWatchedListKey = booleanPreferencesKey("hide_today_watched_list")

    override suspend fun updateDynamicTheme(theme: DynamicTheme) {
        dataStore.edit { pref ->
            pref[dynamicThemeKey] = theme.ordinal
        }
    }

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        dataStore.edit { pref ->
            pref[themeModeKey] = themeMode.ordinal
        }
    }

    override suspend fun updateLanguage(language: Language) {
        dataStore.edit { pref ->
            pref[languageKey] = language.ordinal
        }
    }

    override suspend fun getDynamicTheme(): DynamicTheme? {
        val mode = dataStore.data.map { pref ->
            pref[dynamicThemeKey]
        }.firstOrNull()

        return mode?.let { DynamicTheme.getByValue(it) }
    }

    override suspend fun getThemeMode(): ThemeMode? {
        val mode = dataStore.data.map { pref ->
            pref[themeModeKey]
        }.firstOrNull()

        return mode?.let { ThemeMode.getByValue(it) }
    }

    override suspend fun getLanguage(): Language? {
        val mode = dataStore.data.map { pref ->
            pref[languageKey]
        }.firstOrNull()

        return mode?.let { Language.getByValue(it) }
    }

    override fun getHideTodayWatchedList(): Flow<Boolean> = dataStore.data.map {
        pref -> pref[hideTodayWatchedListKey] ?: false
    }

    override suspend fun updateHideTodayWatchedList(hideTodayWatchedList: Boolean) {
        dataStore.edit { pref ->
            pref[hideTodayWatchedListKey] = hideTodayWatchedList
        }
    }
}