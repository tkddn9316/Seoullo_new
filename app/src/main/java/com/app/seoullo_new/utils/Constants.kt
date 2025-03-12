package com.app.seoullo_new.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.util.toRange
import com.app.domain.model.theme.DynamicTheme
import com.app.domain.model.theme.Language
import com.app.domain.model.theme.ThemeMode
import com.app.seoullo_new.R

/**
 * presentation 계층에서 사용하는 상수 및 enum class 모음
 */
object Constants {
    const val INTENT_DATA = "_data"
    const val VALUE_YES = "Y"
    const val SELECTED_TOUR_LIST = 0
    const val SELECTED_NEARBY_LIST = 1
    const val TODAY_WATCHED_LIST_VISIBILITY_SIZE = 5

    fun getTabTitle(language: Language): List<String> {
        return if (language == Language.KOREA) {
            listOf("홈", "여행", "설정")
        } else {
            listOf("Home", "Travel", "Setting")
        }
    }

    @Composable
    fun getDynamicThemeTitle(theme: DynamicTheme) = when (theme) {
        DynamicTheme.ON -> stringResource(R.string.on)
        DynamicTheme.OFF -> stringResource(R.string.off)
    }

    @Composable
    fun getThemeModeTitle(theme: ThemeMode) = when (theme) {
        ThemeMode.SYSTEM -> stringResource(R.string.system_default)
        ThemeMode.DARK -> stringResource(R.string.on)
        ThemeMode.LIGHT -> stringResource(R.string.off)
    }

    @Composable
    fun getLanguageTitle(language: Language) = when (language) {
        Language.ENGLISH -> stringResource(R.string.language_ENGLISH)
        Language.KOREA -> stringResource(R.string.language_KOREA)
    }

    enum class SortCriteria {
        RATING, REVIEW
    }

    enum class FocusedField {
        STARTING, DESTINATION
    }

    enum class WeatherStatus(val id: IntRange) {
        Thunderstorm(200..299),
        Drizzle(300..399),
        Rain(500..599),
        Snow(600..699),
        Atmosphere(700..799),
        Clear(800..800),
        Clouds(801..899);

        companion object {
            // 범위 찾기
            fun fromId(id: Int): WeatherStatus? {
                return entries.find { id in it.id.toRange() }
            }
        }

        fun getIconRes(): Int {
            return when (this) {
                Clear -> R.raw.weather_sun
                Rain, Drizzle -> R.raw.weather_rainy_sun
                Snow -> R.raw.weather_snowy
                Thunderstorm -> R.raw.weather_thunderstorm
                Clouds, Atmosphere -> R.raw.weather_cloudy
            }
        }
    }
}