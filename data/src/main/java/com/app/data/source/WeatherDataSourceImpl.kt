package com.app.data.source

import com.app.data.api.ApiInterface3
import com.app.data.model.WeatherDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherDataSourceImpl @Inject constructor(private val apiInterface: ApiInterface3) :
    WeatherDataSource {
    override fun getWeather(
        apiKey: String
    ): Flow<WeatherDTO> {
        return flow {
            emit(
                apiInterface.getWeather(
                    apiKey = apiKey
                )
            )
        }
    }
}