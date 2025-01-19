package com.app.data.repository

import com.app.data.mapper.mapperToWeather
import com.app.data.source.DustDataSource
import com.app.data.source.WeatherDataSource
import com.app.domain.model.Weather
import com.app.domain.repository.WeatherRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.retry
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataSource: WeatherDataSource,
    private val dustDataSource: DustDataSource
) : WeatherRepository {

    override fun getWeather(
        weatherApiKey: String,
        dustApiKey: String,
        languageCode: String
    ): Flow<Weather> {
        // 병렬 처리
        val weatherFlow = weatherDataSource.getWeather(
            apiKey = weatherApiKey,
            languageCode = languageCode
        )
            .retry(3) {
                delay(500)
                it is IOException
            }
            .catch { throw it }
        val dustFlow = dustDataSource.getDustData(
            apiKey = dustApiKey
        )
            .retry(3) {
                delay(500)
                it is IOException
            }
            .catch { throw it }

        return combine(weatherFlow, dustFlow) { weatherData, dustData ->
            val updatedData = weatherData.copy(
                fineDust = dustData.result.items.firstOrNull()?.fineDust ?: -1,
                ultraFineDust = dustData.result.items.firstOrNull()?.ultraFineDust ?: -1
            )
            mapperToWeather(updatedData)
        }
    }
}