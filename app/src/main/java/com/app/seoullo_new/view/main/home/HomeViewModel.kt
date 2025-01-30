package com.app.seoullo_new.view.main.home

import android.Manifest
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import com.app.domain.model.Weather
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.CheckingManager
import com.app.seoullo_new.view.base.BaseViewModel2
import com.app.seoullo_new.view.ui.theme.Color_Weather_Sunny_Afternoon1
import com.app.seoullo_new.view.ui.theme.Color_Weather_Sunny_Afternoon2
import com.app.seoullo_new.view.util.WeatherUIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    private val weatherUIRepository: WeatherUIRepository,
    private val checkingManager: CheckingManager
) : BaseViewModel2(dispatcherProvider) {
    private val json: String = checkNotNull(savedStateHandle["weather"])
    private val weather: Weather by lazy { Json.decodeFromString<Weather>(json) }
    private val _weatherResult = MutableStateFlow(weather)
    val weatherResult = _weatherResult.asStateFlow()

    private val _homeBackgroundColor = MutableStateFlow(listOf(Color_Weather_Sunny_Afternoon1, Color_Weather_Sunny_Afternoon2))
    val homeBackgroundColor = _homeBackgroundColor.asStateFlow()

    private val _weatherIcon = MutableStateFlow(0)
    val weatherIcon = _weatherIcon.asStateFlow()

    private val _errorMessages = MutableStateFlow<String?>(null)
    val errorMessages = _errorMessages.asStateFlow()

    init {
        checkPermission()
        _homeBackgroundColor.value = setHomeBackgroundColor(weather)
        _weatherIcon.value = setWeatherIcon(weather)
    }

    private fun checkPermission() {
        onIO {
            checkingManager.checkPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun setHomeBackgroundColor(item: Weather): List<Color> =
        weatherUIRepository.setWeatherColor(weather = item)

    private fun setWeatherIcon(item: Weather): Int =
        weatherUIRepository.setWeatherIcon(weather = item)
}