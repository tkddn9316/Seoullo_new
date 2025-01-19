package com.app.seoullo_new.view.main.home

import android.Manifest
import com.app.domain.model.Weather
import com.app.domain.model.common.ApiState
import com.app.domain.usecase.weather.WeatherUseCase
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.CheckingManager
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.base.BaseViewModel2
import com.app.seoullo_new.view.ui.theme.Color_Weather_Sunny_Afternoon1
import com.app.seoullo_new.view.ui.theme.Color_Weather_Sunny_Afternoon2
import com.app.seoullo_new.view.util.WeatherUIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val weatherUIRepository: WeatherUIRepository,
    private val checkingManager: CheckingManager,
    private val weatherUseCase: WeatherUseCase
) : BaseViewModel2(dispatcherProvider) {
    private val _weatherResult = MutableStateFlow(Weather())
    val weatherResult = _weatherResult.asStateFlow()

    private val _homeBackgroundColor = MutableStateFlow(listOf(Color_Weather_Sunny_Afternoon1, Color_Weather_Sunny_Afternoon2))
    val homeBackgroundColor = _homeBackgroundColor.asStateFlow()

    private val _weatherIcon = MutableStateFlow(0)
    val weatherIcon = _weatherIcon.asStateFlow()

    private val _errorMessages = MutableStateFlow<String?>(null)
    val errorMessages = _errorMessages.asStateFlow()

    init {
        checkPermission()
        getWeatherList("ko")
    }

    private fun checkPermission() {
        onIO {
            checkingManager.checkPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    // TODO: 스플래시에서 날씨 정보 가지고 오도록?
    private fun getWeatherList(
        languageCode: String
    ) {
        onIO {
            weatherUseCase(
                weatherApiKey = BuildConfig.OPEN_WEATHER_MAP_KEY,
                dustApiKey = BuildConfig.SEOUL_OPEN_API_KEY,
                languageCode = languageCode
            ).flowOn(Dispatchers.IO)
                .catch { Logging.e(it.message ?: "") }
                .collect { state ->
                    when (state) {
                        is ApiState.Success -> {
                            val weather = state.data ?: Weather()
                            _weatherResult.value = weather
//                            _homeBackgroundColor.value = setHomeBackgroundColor(weatherList)
//                            _weatherIcon.value = setWeatherIcon(weatherList)
                            Logging.e(weather)
                        }
                        is ApiState.Error -> {
                            val errorMessage = state.message
                            if (_errorMessages.value != errorMessage) {
                                _errorMessages.value = errorMessage
                            }
                        }
                        else -> {}
                    }
                }
        }
    }

//    fun setHomeBackgroundColor(items: List<Weather>): List<Color> =
//        weatherUIRepository.setColor(items = items)
//
//    fun setWeatherIcon(items: List<Weather>): Int =
//        weatherUIRepository.setWeatherIcon(items = items)
}