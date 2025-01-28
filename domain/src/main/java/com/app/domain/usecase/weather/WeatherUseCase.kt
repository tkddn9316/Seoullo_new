package com.app.domain.usecase.weather

import com.app.domain.model.Weather
import com.app.domain.model.common.ApiState
import com.app.domain.repository.WeatherRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class WeatherUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    operator fun invoke(
        weatherApiKey: String,
        dustApiKey: String,
        sunriseApiKey: String
    ): Flow<ApiState<Weather>> = flow {
        emit(ApiState.Loading())
        try {
            weatherRepository.getWeather(
                weatherApiKey = weatherApiKey,
                dustApiKey = dustApiKey,
                sunriseApiKey = sunriseApiKey
            ).collect {
                emit(ApiState.Success(it))
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is IOException -> "${e.message}"
                is JsonSyntaxException -> "Parsing error: Received non-JSON response (possibly HTML)."
                else -> "${e.message}"
            }
            emit(ApiState.Error(errorMessage))
        }
    }
}