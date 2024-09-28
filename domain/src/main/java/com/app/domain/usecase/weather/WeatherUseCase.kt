package com.app.domain.usecase.weather

import com.app.domain.model.Weather
import com.app.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    operator fun invoke(
        serviceKey: String,
        baseDate: String
    ): Flow<List<Weather>> = weatherRepository.getWeather(serviceKey, baseDate)
}