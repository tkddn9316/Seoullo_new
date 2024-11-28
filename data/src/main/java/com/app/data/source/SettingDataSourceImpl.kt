package com.app.data.source

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.app.domain.model.theme.DynamicTheme
import com.app.domain.model.theme.Language
import com.app.domain.model.theme.ThemeMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingDataSource {
    private val dynamicThemeKey = intPreferencesKey("dynamic_mode")
    private val themeModeKey = intPreferencesKey("theme_mode")
    private val languageKey = intPreferencesKey("setting_language")

    override suspend fun updateDynamicTheme(theme: DynamicTheme) {
        dataStore.edit { pref ->
            pref[dynamicThemeKey] = theme.ordinal
        }

        dataStore.data.collect { preferences ->
            preferences.asMap().forEach { (key, value) ->
                Log.d("DataStore", "Key: ${key.name}, Value: $value")
            }
        }
    }

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        dataStore.edit { pref ->
            pref[themeModeKey] = themeMode.ordinal
        }

        dataStore.data.collect { preferences ->
            preferences.asMap().forEach { (key, value) ->
                Log.d("DataStore", "Key: ${key.name}, Value: $value")
            }
        }
    }

    override suspend fun getDynamicTheme(): DynamicTheme? {
        val mode = dataStore.data.map { pref ->
            pref[dynamicThemeKey]
        }.first() ?: return null

        return DynamicTheme.getByValue(mode)
    }

    override suspend fun getThemeMode(): ThemeMode? {
        val mode = dataStore.data.map { pref ->
            pref[themeModeKey]
        }.first() ?: return null

        return ThemeMode.getByValue(mode)
    }

    override suspend fun updateLanguage(language: Language) {
        dataStore.edit { pref ->
            pref[languageKey] = language.ordinal
        }
    }

    override suspend fun getLanguage(): Language? {
        val mode = dataStore.data.map { pref ->
            pref[languageKey]
        }.firstOrNull()

        return mode?.let { Language.getByValue(it) }
    }
}