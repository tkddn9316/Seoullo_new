package com.app.data.model

import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    @SerializedName("current")
    val current: Current,
    @SerializedName("daily")
    val daily: List<Daily>,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lng: Double,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int,
    @SerializedName("cod")
    val errorCode: Int?,
    @SerializedName("message")
    val errorMessage: String?,
    var fineDust: Int = -1,  // 미세먼지
    var ultraFineDust: Int = -1  // 초미세먼지
) {
    data class Current(
        @SerializedName("clouds")
        val clouds: Int,        // 흐림, %
        @SerializedName("dew_point")
        val dewPoint: Double,   // 대기 온도
        @SerializedName("dt")
        val currentTime: Int,   // 현재 시간, Unix, UTC
        @SerializedName("feels_like")
        val feelsLike: Double,  // 체감온도
        @SerializedName("humidity")
        val humidity: Int,      // 습도
        @SerializedName("pressure")
        val pressure: Int,      // 대기압
        @SerializedName("sunrise")
        val sunrise: Int,       // 일출 시간, Unix, UTC
        @SerializedName("sunset")
        val sunset: Int,        // 일몰 시간, Unix, UTC
        @SerializedName("temp")
        val temp: Double,       // 온도
        @SerializedName("uvi")
        val uvi: Double,        // 현재 자외선 지수
        @SerializedName("visibility")
        val visibility: Int,    // 평균 가시성, 미터. 가시성의 최대값은 10km
        @SerializedName("weather")
        val weather: List<Weather>,
        @SerializedName("wind_deg")
        val windDeg: Int,
        @SerializedName("wind_gust")
        val windGust: Double?,
        @SerializedName("wind_speed")
        val windSpeed: Double,
        @SerializedName("rain")
        val rain: Rain?         // 1시간 강수량
    ) {
        data class Weather(
            @SerializedName("description")
            val description: String,
            @SerializedName("icon")
            val icon: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("main")
            val main: String
        )

        data class Rain(
            @SerializedName("1h")
            val precipitation: Double
        )
    }

    data class Daily(
        @SerializedName("clouds")
        val clouds: Int,
        @SerializedName("dew_point")
        val dewPoint: Double,
        @SerializedName("dt")
        val dt: Int,
        @SerializedName("feels_like")
        val feelsLike: FeelsLike,
        @SerializedName("humidity")
        val humidity: Int,
        @SerializedName("moon_phase")
        val moonPhase: Double,
        @SerializedName("moonrise")
        val moonrise: Int,  // 일출 시간
        @SerializedName("moonset")
        val moonset: Int,   // 일몰 시간
        @SerializedName("pop")
        val pop: Double,    // 강수 확률
        @SerializedName("pressure")
        val pressure: Int,
        @SerializedName("summary")
        val summary: String,
        @SerializedName("sunrise")
        val sunrise: Int,
        @SerializedName("sunset")
        val sunset: Int,
        @SerializedName("temp")
        val temp: Temp,
        @SerializedName("uvi")
        val uvi: Double,
        @SerializedName("weather")
        val weather: List<Weather>,
        @SerializedName("wind_deg")
        val windDeg: Int,
        @SerializedName("wind_gust")
        val windGust: Double?,
        @SerializedName("wind_speed")
        val windSpeed: Double
    ) {
        data class FeelsLike(
            @SerializedName("day")
            val day: Double,
            @SerializedName("eve")
            val eve: Double,
            @SerializedName("morn")
            val morn: Double,
            @SerializedName("night")
            val night: Double
        )

        data class Temp(
            @SerializedName("day")
            val day: Double,
            @SerializedName("eve")
            val eve: Double,
            @SerializedName("max")
            val max: Double,
            @SerializedName("min")
            val min: Double,
            @SerializedName("morn")
            val morn: Double,
            @SerializedName("night")
            val night: Double
        )

        data class Weather(
            @SerializedName("description")
            val description: String,
            @SerializedName("icon")
            val icon: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("main")
            val main: String
        )
    }
}

// https://openweathermap.org/api/one-call-3#current
//
//{
//  "lat": 37.567,
//  "lon": 126.9786,
//  "timezone": "Asia/Seoul",
//  "timezone_offset": 32400,
//  "current": {
//    "dt": 1737226050,
//    "sunrise": 1737240255,
//    "sunset": 1737276048,
//    "temp": -0.1,
//    "feels_like": -0.1,
//    "pressure": 1020,
//    "humidity": 33,
//    "dew_point": -12.91,
//    "uvi": 0,
//    "clouds": 21,
//    "visibility": 10000,
//    "wind_speed": 1.03,
//    "wind_deg": 133,
//    "wind_gust": 0.94,
//    "weather": [
//      {
//        "id": 801,
//        "main": "Clouds",
//        "description": "few clouds",
//        "icon": "02n"
//      }
//    ]
//  },
//  "daily": [
//    {
//      "dt": 1737255600,
//      "sunrise": 1737240255,
//      "sunset": 1737276048,
//      "moonrise": 1737295320,
//      "moonset": 1737250440,
//      "moon_phase": 0.67,
//      "summary": "There will be clear sky until morning, then partly cloudy",
//      "temp": {
//        "day": 1.16,
//        "min": -0.15,
//        "max": 3.02,
//        "night": 0.64,
//        "eve": 1.18,
//        "morn": -0.15
//      },
//      "feels_like": {
//        "day": -0.18,
//        "night": 0.64,
//        "eve": 1.18,
//        "morn": -0.15
//      },
//      "pressure": 1019,
//      "humidity": 39,
//      "dew_point": -11.15,
//      "wind_speed": 1.63,
//      "wind_deg": 120,
//      "wind_gust": 2.78,
//      "weather": [
//        {
//          "id": 804,
//          "main": "Clouds",
//          "description": "overcast clouds",
//          "icon": "04d"
//        }
//      ],
//      "clouds": 100,
//      "pop": 0,
//      "uvi": 0
//    },
//    {
//      "dt": 1737342000,
//      "sunrise": 1737326628,
//      "sunset": 1737362512,
//      "moonrise": 1737385200,
//      "moonset": 1737338100,
//      "moon_phase": 0.7,
//      "summary": "You can expect partly cloudy in the morning, with clearing in the afternoon",
//      "temp": {
//        "day": 3.95,
//        "min": 0.6,
//        "max": 5.27,
//        "night": 1.34,
//        "eve": 3.83,
//        "morn": 1.16
//      },
//      "feels_like": {
//        "day": 3.95,
//        "night": 1.34,
//        "eve": 3.83,
//        "morn": 1.16
//      },
//      "pressure": 1019,
//      "humidity": 60,
//      "dew_point": -3.16,
//      "wind_speed": 1.17,
//      "wind_deg": 105,
//      "wind_gust": 1.13,
//      "weather": [
//        {
//          "id": 804,
//          "main": "Clouds",
//          "description": "overcast clouds",
//          "icon": "04d"
//        }
//      ],
//      "clouds": 95,
//      "pop": 0,
//      "uvi": 0
//    },
//    {
//      "dt": 1737428400,
//      "sunrise": 1737412999,
//      "sunset": 1737448977,
//      "moonrise": 0,
//      "moonset": 1737425820,
//      "moon_phase": 0.73,
//      "summary": "Expect a day of partly cloudy with clear spells",
//      "temp": {
//        "day": 3.1,
//        "min": 0.08,
//        "max": 5.06,
//        "night": 1.52,
//        "eve": 3.9,
//        "morn": 0.22
//      },
//      "feels_like": {
//        "day": 3.1,
//        "night": 1.52,
//        "eve": 3.9,
//        "morn": 0.22
//      },
//      "pressure": 1024,
//      "humidity": 65,
//      "dew_point": -2.8,
//      "wind_speed": 1.29,
//      "wind_deg": 270,
//      "wind_gust": 1.97,
//      "weather": [
//        {
//          "id": 804,
//          "main": "Clouds",
//          "description": "overcast clouds",
//          "icon": "04d"
//        }
//      ],
//      "clouds": 99,
//      "pop": 0,
//      "uvi": 0
//    },
//    {
//      "dt": 1737514800,
//      "sunrise": 1737499368,
//      "sunset": 1737535442,
//      "moonrise": 1737475140,
//      "moonset": 1737513660,
//      "moon_phase": 0.75,
//      "summary": "The day will start with clear sky through the late morning hours, transitioning to partly cloudy",
//      "temp": {
//        "day": 2.88,
//        "min": 0.37,
//        "max": 4.51,
//        "night": 3.06,
//        "eve": 3.78,
//        "morn": 0.37
//      },
//      "feels_like": {
//        "day": 2.88,
//        "night": 3.06,
//        "eve": 3.78,
//        "morn": 0.37
//      },
//      "pressure": 1027,
//      "humidity": 59,
//      "dew_point": -4.41,
//      "wind_speed": 1.01,
//      "wind_deg": 88,
//      "wind_gust": 1.15,
//      "weather": [
//        {
//          "id": 804,
//          "main": "Clouds",
//          "description": "overcast clouds",
//          "icon": "04d"
//        }
//      ],
//      "clouds": 100,
//      "pop": 0,
//      "uvi": 0
//    },
//    {
//      "dt": 1737601200,
//      "sunrise": 1737585735,
//      "sunset": 1737621908,
//      "moonrise": 1737565140,
//      "moonset": 1737601620,
//      "moon_phase": 0.79,
//      "summary": "You can expect partly cloudy in the morning, with clearing in the afternoon",
//      "temp": {
//        "day": 4.44,
//        "min": 0.75,
//        "max": 6.36,
//        "night": 3.71,
//        "eve": 5.5,
//        "morn": 0.75
//      },
//      "feels_like": {
//        "day": 4.44,
//        "night": 3.71,
//        "eve": 4.44,
//        "morn": 0.75
//      },
//      "pressure": 1024,
//      "humidity": 57,
//      "dew_point": -3.32,
//      "wind_speed": 1.56,
//      "wind_deg": 301,
//      "wind_gust": 1.9,
//      "weather": [
//        {
//          "id": 801,
//          "main": "Clouds",
//          "description": "few clouds",
//          "icon": "02d"
//        }
//      ],
//      "clouds": 13,
//      "pop": 0,
//      "uvi": 0
//    },
//    {
//      "dt": 1737687600,
//      "sunrise": 1737672100,
//      "sunset": 1737708374,
//      "moonrise": 1737655260,
//      "moonset": 1737690000,
//      "moon_phase": 0.82,
//      "summary": "There will be clear sky today",
//      "temp": {
//        "day": 3.79,
//        "min": 0.41,
//        "max": 5.77,
//        "night": 2.64,
//        "eve": 4.83,
//        "morn": 0.41
//      },
//      "feels_like": {
//        "day": 2.71,
//        "night": 1.02,
//        "eve": 2.02,
//        "morn": 0.41
//      },
//      "pressure": 1023,
//      "humidity": 38,
//      "dew_point": -9.09,
//      "wind_speed": 3.4,
//      "wind_deg": 323,
//      "wind_gust": 5.77,
//      "weather": [
//        {
//          "id": 800,
//          "main": "Clear",
//          "description": "clear sky",
//          "icon": "01d"
//        }
//      ],
//      "clouds": 0,
//      "pop": 0,
//      "uvi": 0
//    },
//    {
//      "dt": 1737774000,
//      "sunrise": 1737758464,
//      "sunset": 1737794840,
//      "moonrise": 1737745440,
//      "moonset": 1737778800,
//      "moon_phase": 0.85,
//      "summary": "You can expect clear sky in the morning, with partly cloudy in the afternoon",
//      "temp": {
//        "day": 3.48,
//        "min": -0.02,
//        "max": 5.61,
//        "night": 2.9,
//        "eve": 4.98,
//        "morn": -0.02
//      },
//      "feels_like": {
//        "day": 3.48,
//        "night": 2.9,
//        "eve": 4.98,
//        "morn": -0.02
//      },
//      "pressure": 1026,
//      "humidity": 48,
//      "dew_point": -6.39,
//      "wind_speed": 1.27,
//      "wind_deg": 346,
//      "wind_gust": 1.76,
//      "weather": [
//        {
//          "id": 800,
//          "main": "Clear",
//          "description": "clear sky",
//          "icon": "01d"
//        }
//      ],
//      "clouds": 1,
//      "pop": 0,
//      "uvi": 0
//    },
//    {
//      "dt": 1737860400,
//      "sunrise": 1737844825,
//      "sunset": 1737881307,
//      "moonrise": 1737835560,
//      "moonset": 1737868140,
//      "moon_phase": 0.88,
//      "summary": "You can expect partly cloudy in the morning, with snow in the afternoon",
//      "temp": {
//        "day": 2.97,
//        "min": -2.61,
//        "max": 2.97,
//        "night": -2.61,
//        "eve": -0.66,
//        "morn": 1.63
//      },
//      "feels_like": {
//        "day": -0.38,
//        "night": -8.69,
//        "eve": -6.37,
//        "morn": -0.01
//      },
//      "pressure": 1018,
//      "humidity": 71,
//      "dew_point": -1.76,
//      "wind_speed": 6.87,
//      "wind_deg": 289,
//      "wind_gust": 11.68,
//      "weather": [
//        {
//          "id": 601,
//          "main": "Snow",
//          "description": "snow",
//          "icon": "13d"
//        }
//      ],
//      "clouds": 100,
//      "pop": 1,
//      "snow": 2.52,
//      "uvi": 0
//    }
//  ]
//}