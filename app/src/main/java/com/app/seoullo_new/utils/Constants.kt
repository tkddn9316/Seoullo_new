package com.app.seoullo_new.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
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

    enum class WeatherCategory(val category: String) {
        Sky("SKY"),     // 하늘 상태
        PTY("PTY"),     // 강수 형태
        Temperature("TMP"),     // 1시간 기온
        UUU("UUU"),     // 풍속(동서성분)
        VVV("VVV"),     // 풍속(남북성분)
        WindDirection("VEC"),     // 풍향
        WindSpeed("WSD"),   // 풍속
        POP("POP"),     // 강수확률
        Wave("WAV"),     // 파고
        Precipitation("PCP"),   // 강수량
        Humidity("REH"),     // 습도
        Snow("SNO")     // 눈의 양
    }

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
}