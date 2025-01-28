package com.app.data.repository

import com.app.data.mapper.mapperToWeather
import com.app.data.source.DustDataSource
import com.app.data.source.SunriseDataSource
import com.app.data.source.WeatherDataSource
import com.app.domain.model.Weather
import com.app.domain.repository.WeatherRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.retry
import org.joda.time.DateTime
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataSource: WeatherDataSource,
    private val dustDataSource: DustDataSource,
    private val sunriseDataSource: SunriseDataSource
) : WeatherRepository {

    override fun getWeather(
        weatherApiKey: String,
        dustApiKey: String,
        sunriseApiKey: String
    ): Flow<Weather> {
        // 병렬 처리
        val weatherFlow = weatherDataSource.getWeather(
            apiKey = weatherApiKey
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
        val sunriseFlow = sunriseDataSource.getSunriseTime(
            currentDate = DateTime.now().toString("yyyyMMdd"),
            apiKey = sunriseApiKey
        )
            .retry(3) {
                delay(500)
                it is IOException
            }
            .catch { throw it }

        return combine(weatherFlow, dustFlow, sunriseFlow) { weatherData, dustData, sunriseData ->
            val updatedData = weatherData.copy(
                fineDust = dustData.result.items.firstOrNull()?.fineDust ?: -1,
                ultraFineDust = dustData.result.items.firstOrNull()?.ultraFineDust ?: -1,
                sunrise = sunriseData.body?.items?.itemList?.firstOrNull()?.sunrise ?: "",
                sunset = sunriseData.body?.items?.itemList?.firstOrNull()?.sunset ?: ""
            )
            mapperToWeather(updatedData)
        }
    }
}