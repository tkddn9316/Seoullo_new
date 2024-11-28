package com.app.data.repository

import com.app.data.source.SettingDataSource
import com.app.data.utils.Logging
import com.app.domain.model.theme.DynamicTheme
import com.app.domain.model.theme.Language
import com.app.domain.model.theme.ThemeMode
import com.app.domain.model.theme.ThemeSetting
import com.app.domain.repository.SettingRepository
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val settingDataSource: SettingDataSource
) : SettingRepository {
    override suspend fun fetchThemes(): ThemeSetting = ThemeSetting(
        dynamicTheme = settingDataSource.getDynamicTheme() ?: DynamicTheme.OFF,
        themeMode = settingDataSource.getThemeMode() ?: ThemeMode.SYSTEM,
        language = settingDataSource.getLanguage() ?: Language.ENGLISH
    )

//    override suspend fun fetchThemes(): ThemeSetting {
//        Logging.e(settingDataSource.getDynamicTheme() ?: "노ㅓㄹ이다")
//        Logging.e(settingDataSource.getThemeMode() ?: "노ㅓㄹ이다")
//        Logging.e(settingDataSource.getLanguage() ?: "노ㅓㄹ이다")
//        return ThemeSetting(
//            dynamicTheme = settingDataSource.getDynamicTheme() ?: DynamicTheme.OFF,
//            themeMode = settingDataSource.getThemeMode() ?: ThemeMode.SYSTEM,
//            language = settingDataSource.getLanguage() ?: Language.ENGLISH
//        )
//    }

    override suspend fun updateThemes(themeSetting: ThemeSetting) {
        settingDataSource.updateDynamicTheme(themeSetting.dynamicTheme)
        settingDataSource.updateThemeMode(themeSetting.themeMode)
    }

//    override suspend fun fetchLanguage(): ThemeSetting = ThemeSetting(
//        language = settingDataSource.getLanguage() ?: Language.ENGLISH
//    )

//    override suspend fun fetchLanguage(): ThemeSetting {
//        Logging.e(settingDataSource.getLanguage() ?: "노ㅓㄹ이다")
//        return ThemeSetting(dynamicTheme = settingDataSource.getDynamicTheme() ?: DynamicTheme.OFF,
//            themeMode = settingDataSource.getThemeMode() ?: ThemeMode.SYSTEM,
//            language = settingDataSource.getLanguage() ?: Language.ENGLISH)
//    }

    override suspend fun updateLanguage(themeSetting: ThemeSetting) {
        settingDataSource.updateLanguage(themeSetting.language)
    }
}