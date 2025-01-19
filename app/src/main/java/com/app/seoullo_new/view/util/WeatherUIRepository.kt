package com.app.seoullo_new.view.util

class WeatherUIRepository {

    // 배경 색
//    fun setColor(items: List<Weather>): List<Color> {
//        val defaultColor = listOf(Color_Weather_Sunny_Afternoon1, Color_Weather_Sunny_Afternoon2)
//        if (items.isNotEmpty()) {
//            val sky = items.find { it.category == WeatherCategory.Sky.category }
//            val pty = items.find { it.category == WeatherCategory.PTY.category }
//
//            if (sky == null || pty == null) {
//                return defaultColor
//            }
//
//            // Sky = 맑음(1), 구름많음(3), 흐림(4)
//            // PTY = 강수형태(0=없음, 1=비, 2=비/눈, 3=눈, 4=소나기)
//            val skyValue = sky.fcstValue.toIntOrNull() ?: -1
//            val ptyValue = pty.fcstValue.toIntOrNull() ?: -1
//
//            // TODO: API 통하여 일출/일몰 시간 가져오기 필요
//            val currentTime = DateTime.now().toString("HH:mm:ss")
//            Logging.e("currentTime: $currentTime")
//
//            val backgroundColor =
//                when (currentTime) {
//                    in "07:00:00".."16:59:59" -> {
//                        // 아침 and 낮
//                        when {
//                            (ptyValue == 2 || ptyValue == 3) -> listOf(
//                                Color_Weather_Snowy,
//                                Color_Weather_Snowy
//                            )
//
//                            (ptyValue == 1 || ptyValue == 4) -> listOf(
//                                Color_Weather_Rainy,
//                                Color_Weather_Rainy
//                            )
//
//                            ptyValue == 0 -> {
//                                when (skyValue) {
//                                    1 -> listOf(        // 맑음
//                                        Color_Weather_Sunny_Afternoon1,
//                                        Color_Weather_Sunny_Afternoon2
//                                    )
//                                    3, 4 -> listOf(     // 구름많음(3), 흐림(4)
//                                        Color_Weather_Cloudy,
//                                        Color_Weather_Cloudy
//                                    )
//                                    else -> defaultColor
//                                }
//                            }
//
//                            else -> defaultColor
//                        }
//                    }
//
//                    in "17:00:00".."18:59:59" -> {
//                        // 저녁
//                        listOf(Color_Weather_Sunny_Dinner1, Color_Weather_Sunny_Dinner2)
//                    }
//
//                    else -> {
//                        // 밤
//                        listOf(Color_Weather_Sunny_Night1, Color_Weather_Sunny_Night2)
//                    }
//                }
//
//            return backgroundColor
//        } else {
//            return defaultColor
//        }
//    }

    // 아이콘
//    fun setWeatherIcon(items: List<Weather>): Int {
//        if (items.isNotEmpty()) {
//            val sky = items.find { it.category == WeatherCategory.Sky.category }
//            val pty = items.find { it.category == WeatherCategory.PTY.category }
//
//            if (sky == null || pty == null) {
//                return 0
//            }
//
//            // Sky = 맑음(1), 구름많음(3), 흐림(4)
//            // PTY = 강수형태(0=없음, 1=비, 2=비/눈, 3=눈, 4=소나기)
//            val skyValue = sky.fcstValue.toIntOrNull() ?: -1
//            val ptyValue = pty.fcstValue.toIntOrNull() ?: -1
//
//            // TODO: API 통하여 일출/일몰 시간 가져오기 필요
//            val currentTime = DateTime.now().toString("HH:mm:ss")
//            Logging.e("currentTime: $currentTime")
//
//            val weatherIcon = when {
//                (ptyValue == 2 || ptyValue == 3) -> R.raw.weather_snowy
//                (ptyValue == 1 || ptyValue == 4) -> when (currentTime) {
//                    in "07:00:00".."18:59:59" -> R.raw.weather_rainy_sun
//                    else -> R.raw.weather_rainy_night
//                }
//                ptyValue == 0 -> {
//                    when (skyValue) {
//                        1 -> when (currentTime) {   // 맑음
//                            in "07:00:00".."18:59:59" -> R.raw.weather_sun
//                            else -> R.raw.weather_moon
//                        }
//
//                        3, 4 -> R.raw.weather_cloudy  // 구름많음(3), 흐림(4)
//                        else -> 0
//                    }
//                }
//
//                else -> 0
//            }
//
//            return weatherIcon
//        } else {
//            return 0
//        }
//    }
}