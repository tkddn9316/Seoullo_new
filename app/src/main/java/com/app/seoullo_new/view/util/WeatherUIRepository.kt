package com.app.seoullo_new.view.util

import androidx.compose.ui.graphics.Color
import com.app.domain.model.Weather
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Constants.WeatherStatus
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.ui.theme.Color_Weather_Cloudy
import com.app.seoullo_new.view.ui.theme.Color_Weather_Rainy
import com.app.seoullo_new.view.ui.theme.Color_Weather_Snowy
import com.app.seoullo_new.view.ui.theme.Color_Weather_Sunny_Afternoon1
import com.app.seoullo_new.view.ui.theme.Color_Weather_Sunny_Afternoon2
import com.app.seoullo_new.view.ui.theme.Color_Weather_Sunny_Dinner1
import com.app.seoullo_new.view.ui.theme.Color_Weather_Sunny_Dinner2
import com.app.seoullo_new.view.ui.theme.Color_Weather_Sunny_Night1
import com.app.seoullo_new.view.ui.theme.Color_Weather_Sunny_Night2
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class WeatherUIRepository {

    // 배경 색
    fun setWeatherColor(weather: Weather): List<Color> {
        val defaultColor = listOf(Color_Weather_Sunny_Afternoon1, Color_Weather_Sunny_Afternoon2)
        val currentTime = DateTime.now()
        val timeFormatter = DateTimeFormat.forPattern("HH:mm")
        val sunrise = runCatching {
            DateTime.parse(weather.sunrise, timeFormatter)
                .withDate(currentTime.toLocalDate())
        }.getOrElse {
            // 기본 0시
            DateTime.now().withTimeAtStartOfDay()
        }
        val sunset = runCatching {
            DateTime.parse(weather.sunset, timeFormatter)
                .withDate(currentTime.toLocalDate())
        }.getOrElse {
            // 기본 18시
            DateTime.now().withTime(18, 0, 0, 0)
        }
        val currentWeather = WeatherStatus.fromId(weather.todayWeatherId) ?: WeatherStatus.Clear

        return when {
            currentTime.isAfter(sunset.plusHours(1)) || currentTime.isBefore(sunrise) -> {
                // 밤
                listOf(Color_Weather_Sunny_Night1, Color_Weather_Sunny_Night2)
            }

            currentTime.isAfter(sunset) -> {
                // 저녁
                listOf(Color_Weather_Sunny_Dinner1, Color_Weather_Sunny_Dinner2)
            }

            else -> {
                // 아침 and 낮
                when {
                    currentWeather == WeatherStatus.Clear -> listOf(
                        Color_Weather_Sunny_Afternoon1,
                        Color_Weather_Sunny_Afternoon2
                    )

                    currentWeather == WeatherStatus.Snow -> listOf(
                        Color_Weather_Snowy,
                        Color_Weather_Snowy
                    )

                    (currentWeather == WeatherStatus.Rain ||
                            currentWeather == WeatherStatus.Drizzle ||
                            currentWeather == WeatherStatus.Thunderstorm) -> listOf(
                        Color_Weather_Rainy,
                        Color_Weather_Rainy
                    )

                    (currentWeather == WeatherStatus.Clouds ||
                            currentWeather == WeatherStatus.Atmosphere) -> listOf(
                        Color_Weather_Cloudy,
                        Color_Weather_Cloudy
                    )

                    else -> defaultColor
                }
            }
        }
    }

    // 아이콘
    fun setWeatherIcon(weather: Weather): Int {
        val currentTime = DateTime.now()
        val timeFormatter = DateTimeFormat.forPattern("HH:mm")
        val sunrise = runCatching {
            DateTime.parse(weather.sunrise, timeFormatter)
                .withDate(currentTime.toLocalDate())
        }.getOrElse {
            // 기본 0시
            DateTime.now().withTimeAtStartOfDay()
        }
        val sunset = runCatching {
            DateTime.parse(weather.sunset, timeFormatter).withDate(currentTime.toLocalDate())
        }.getOrElse {
            // 기본 18시
            DateTime.now().withTime(18, 0, 0, 0)
        }
        Logging.e("setWeatherIcon TIME: $sunrise, $sunset")
        Logging.e("setWeatherIcon TIME: $currentTime")
        val currentWeather = WeatherStatus.fromId(weather.todayWeatherId) ?: WeatherStatus.Clear

        return when {
            (currentWeather == WeatherStatus.Clouds || currentWeather == WeatherStatus.Atmosphere) -> R.raw.weather_cloudy
            (currentWeather == WeatherStatus.Rain || currentWeather == WeatherStatus.Drizzle) -> {
                if (currentTime.isAfter(sunrise) && currentTime.isBefore(sunset)) {
                    R.raw.weather_rainy_sun // 낮
                } else {
                    R.raw.weather_rainy_night // 밤
                }
            }

            currentWeather == WeatherStatus.Snow -> R.raw.weather_snowy
            currentWeather == WeatherStatus.Thunderstorm -> R.raw.weather_thunderstorm
            currentWeather == WeatherStatus.Clear -> {
                if (currentTime.isAfter(sunrise) && currentTime.isBefore(sunset)) {
                    R.raw.weather_sun // 낮
                } else {
                    R.raw.weather_moon // 밤
                }
            }

            else -> 0
        }
    }
}