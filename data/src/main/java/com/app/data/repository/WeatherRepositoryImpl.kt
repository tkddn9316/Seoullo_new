package com.app.data.repository

import com.app.data.mapper.mapperToWeather
import com.app.data.source.WeatherDataSource
import com.app.domain.model.Weather
import com.app.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataSource: WeatherDataSource
) : WeatherRepository {
    override fun getWeather(serviceKey: String, baseDate: String): Flow<List<Weather>> {
        return flow {
            weatherDataSource.getWeather(serviceKey, baseDate)
                .collect {
                    emit(mapperToWeather(it))
                }
        }
    }
}