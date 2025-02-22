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
    const val SELECTED_TOUR_LIST = 0
    const val SELECTED_NEARBY_LIST = 1

    // TourAPI
//    enum class ContentTypeId(val id: String, val cat: String) {
//        // TODO: https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15101753#/
////        LEISURE_SPORTS("75"), TOURIST("76"), TRAFFIC("77"), CULTURE("78"),
////        SHOPPING("79"), ACCOMMODATION("80"), RESTAURANT("82"), FESTIVAL("85")
//        // 음식점
//        KoreanRestaurant("82", "A05020100"), JapaneseRestaurant("82", "A05020300"),
//        ChineseRestaurant("82", "A05020400"), WesternRestaurants("82", "A05020200"),
//        BarAndCafe("82", "A05020900"),
//    }

    // Google Place
//    enum class ContentType(val type: String) {
//        // TODO: https://developers.google.com/maps/documentation/places/web-service/place-types?hl=ko#lodging
//        // 1줄 마다 세트임
//        LEISURE_SPORTS("75"),
//        TOURIST("76"),
//        TRAFFIC("77"),
//        CULTURE("78"),
//        DEPARTMENT_STORE("department_store"), MARKET("market"), CLOTHING_STORE("clothing_store"),
//        HOTEL("hotel"), MOTEL("motel"), GUEST_HOUSE("guest_house"),
//
//        // 음식점(split(|)로 나눠서 보내기)
//        KoreanRestaurant("korean_restaurant"), JapaneseRestaurant("japanese_restaurant|sushi_restaurant"),
//        ChineseRestaurant("chinese_restaurant"), WesternRestaurants("american_restaurant|fast_food_restaurant|french_restaurant|italian_restaurant|mediterranean_restaurant|spanish_restaurant"),
//        BarAndCafe("cafe|cafeteria|bar"),
//
//        FESTIVAL("85")
//    }

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