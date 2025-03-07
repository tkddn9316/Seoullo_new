package com.app.seoullo_new.view.main.home

import android.Manifest
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.domain.model.Places
import com.app.domain.model.TodayWatchedList
import com.app.domain.model.Weather
import com.app.domain.repository.SettingRepository
import com.app.domain.usecase.todayWatchedList.GetTodayWatchedListUseCase
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.CheckingManager
import com.app.seoullo_new.view.base.BaseViewModel2
import com.app.seoullo_new.view.ui.theme.Color_Weather_Sunny_Afternoon1
import com.app.seoullo_new.view.ui.theme.Color_Weather_Sunny_Afternoon2
import com.app.seoullo_new.view.util.DialogState
import com.app.seoullo_new.view.util.WeatherUIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    settingRepository: SettingRepository,
    private val getTodayWatchedListUseCase: GetTodayWatchedListUseCase,
    private val weatherUIRepository: WeatherUIRepository,
    private val checkingManager: CheckingManager
) : BaseViewModel2(dispatcherProvider) {
    private val weatherJson: String = checkNotNull(savedStateHandle["weather"])
    private val weather: Weather by lazy { Json.decodeFromString<Weather>(weatherJson) }
    private val _weatherResult = MutableStateFlow(weather)
    val weatherResult = _weatherResult.asStateFlow()

    private val bannerJson: String = checkNotNull(savedStateHandle["banner"])
    private val banner: List<Places> by lazy { Json.decodeFromString<List<Places>>(bannerJson) }
    private val _bannerResult = MutableStateFlow(banner)
    val bannerResult = _bannerResult.asStateFlow()

    private val _homeBackgroundColor = MutableStateFlow(listOf(Color_Weather_Sunny_Afternoon1, Color_Weather_Sunny_Afternoon2))
    val homeBackgroundColor = _homeBackgroundColor.asStateFlow()

    private val _weatherIcon = MutableStateFlow(0)
    val weatherIcon = _weatherIcon.asStateFlow()

    private val _errorMessages = MutableStateFlow<String?>(null)
    val errorMessages = _errorMessages.asStateFlow()

    private val _todayWatchedList = MutableStateFlow<List<TodayWatchedList>>(emptyList())
    val todayWatchedList = _todayWatchedList.asStateFlow()
    // 오늘 본 목록 숨기기 체크 상태 감지
    val switchState: StateFlow<Boolean> = settingRepository.getShowTodayWatchedList()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _dialogState = MutableStateFlow(DialogState())
    val dialogState: StateFlow<DialogState> = _dialogState.asStateFlow()

    init {
        checkPermission()
        _homeBackgroundColor.value = setHomeBackgroundColor(weather)
        _weatherIcon.value = setWeatherIcon(weather)
    }

    // 오늘 본 목록 전체 팝업
    fun openTodayWatchedListDialog() = _dialogState.update { it.copy(isTodayWatchedListDialogOpen = true) }
    fun closeTodayWatchedListDialog() = _dialogState.update { it.copy(isTodayWatchedListDialogOpen = false) }

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

    fun selectTodayWatchedList() {
        onIO {
            getTodayWatchedListUseCase.select()
                .collect {
                    _todayWatchedList.value = it
                }
        }
    }

    fun toPlaces(data: TodayWatchedList): Places = Places(
        name = data.name,
        id = data.id,
        contentTypeId = data.contentTypeId,
        displayName = data.displayName,
        address = data.address,
        description = data.description,
        openNow = data.openNow,
        weekdayDescriptions = data.weekdayDescriptions,
        rating = data.rating,
        userRatingCount = data.userRatingCount,
        photoUrl = data.photoUrl,
        languageCode = data.languageCode
    )
}