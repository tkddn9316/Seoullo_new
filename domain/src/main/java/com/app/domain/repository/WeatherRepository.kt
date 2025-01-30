package com.app.domain.repository

import com.app.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeather(
        weatherApiKey: String,
        dustApiKey: String,
        sunriseApiKey: String
    ): Flow<Weather>
}