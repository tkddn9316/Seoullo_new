package com.app.domain.model

import com.app.domain.model.common.BaseModel
import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val clouds: Int = 0,
    val dewPoint: Double = 0.0,
    val currentTime: Int = 0,
    val feelsLike: Double = 0.0,
    val humidity: Int = 0,
    val pressure: Int = 0,
    val sunrise: String = "",
    val sunset: String = "",
    val temp: Double = 0.0,
    val uvi: Double = 0.0,
    val todayWeatherId: Int = 0,
    val todayWeatherName: String = "",
    val precipitation: Double = 0.0,
    val dailyList: List<DailyWeather> = emptyList(),
    val fineDust: Int = -1,
    val ultraFineDust: Int = -1,
    val windSpeed: Double = 0.0
) : BaseModel() {
    @Serializable
    data class DailyWeather(
        val dailyWeatherId: Int,
        val dailyWeatherName: String,
        val rainPercent: Double,
        val maxTemp: Double,
        val minTemp: Double,
        val month: String
    ) : BaseModel()
}


//{
//  "lat": 37.567,
//  "lng": 126.9786,
//  "clouds": 21,
//  "dewPoint": -12.91,
//  "currentTime": 1737226050,
//  "feelsLike": -0.1,
//  "humidity": 33,
//  "pressure": 1020,
//  "sunrise": 1737240255,
//  "sunset": 1737276048,
//  "temp": -0.1,
//  "uvi": 0.0,
//  "todayWeatherId": 801,
//  "todayWeatherName": "Clouds",
//  "precipitation": 0.0,
//  "dailyList": [
//    {
//      "dailyWeatherId": 804,
//      "dailyWeatherName": "Clouds",
//      "rainPercent": 0.0,
//      "maxTemp": 3.02,
//      "minTemp": -0.15
//    },
//    {
//      "dailyWeatherId": 804,
//      "dailyWeatherName": "Clouds",
//      "rainPercent": 0.0,
//      "maxTemp": 5.27,
//      "minTemp": 0.6
//    },
//    {
//      "dailyWeatherId": 804,
//      "dailyWeatherName": "Clouds",
//      "rainPercent": 0.0,
//      "maxTemp": 5.06,
//      "minTemp": 0.08
//    },
//    {
//      "dailyWeatherId": 804,
//      "dailyWeatherName": "Clouds",
//      "rainPercent": 0.0,
//      "maxTemp": 4.51,
//      "minTemp": 0.37
//    },
//    {
//      "dailyWeatherId": 801,
//      "dailyWeatherName": "Clouds",
//      "rainPercent": 0.0,
//      "maxTemp": 6.36,
//      "minTemp": 0.75
//    },
//    {
//      "dailyWeatherId": 800,
//      "dailyWeatherName": "Clear",
//      "rainPercent": 0.0,
//      "maxTemp": 5.77,
//      "minTemp": 0.41
//    },
//    {
//      "dailyWeatherId": 800,
//      "dailyWeatherName": "Clear",
//      "rainPercent": 0.0,
//      "maxTemp": 5.61,
//      "minTemp": -0.02
//    },
//    {
//      "dailyWeatherId": 601,
//      "dailyWeatherName": "Snow",
//      "rainPercent": 1.0,
//      "maxTemp": 2.97,
//      "minTemp": -2.61
//    }
//  ]
//}