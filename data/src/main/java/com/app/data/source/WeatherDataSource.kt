package com.app.data.source

import com.app.data.model.WeatherDTO
import kotlinx.coroutines.flow.Flow

interface WeatherDataSource {
    fun getWeather(
        apiKey: String
    ): Flow<WeatherDTO>
}