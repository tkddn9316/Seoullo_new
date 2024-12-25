package com.app.seoullo_new.view.main.home

import android.Manifest
import com.app.domain.model.Direction
import com.app.domain.model.Weather
import com.app.domain.model.common.ApiState
import com.app.domain.usecase.direction.GetDirectionUseCase
import com.app.domain.usecase.weather.WeatherUseCase
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.CheckingManager
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.base.BaseViewModel2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import org.joda.time.DateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val checkingManager: CheckingManager,
    private val weatherUseCase: WeatherUseCase,
    private val directionUseCase: GetDirectionUseCase
) : BaseViewModel2(dispatcherProvider) {
    private val _weatherListResult = MutableStateFlow<List<Weather>>(emptyList())
    val weatherListResult = _weatherListResult.asStateFlow()

    private val _test = MutableStateFlow<ApiState<Direction>>(ApiState.Initial())
    val test = _test.asStateFlow()

    init {
        checkPermission()
        getWeatherList()
    }

    private fun checkPermission() {
        onIO {
            checkingManager.checkPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun getWeatherList() {
        val date = DateTime.now().toString("yyyyMMdd")
        onIO {
            weatherUseCase(
                BuildConfig.TOUR_API_KEY, date
            ).flowOn(Dispatchers.IO)
                .catch { Logging.e(it.message ?: "") }
                .collect {
                    Logging.e(it)
                    _weatherListResult.value = it
                }
        }
    }

    fun test() {
        onIO {
            directionUseCase(
                destination = "37.498194,126.551916",
                starting = "37.495055,126.777836",
                languageCode = "ko",
                apiKey = BuildConfig.SEOULLO_GOOGLE_MAPS_API_KEY
            ).flowOn(Dispatchers.IO)
                .collect { state ->
                    _test.value = state
                }
        }
    }
}