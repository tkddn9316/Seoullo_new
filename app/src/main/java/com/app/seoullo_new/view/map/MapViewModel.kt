package com.app.seoullo_new.view.map

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.domain.model.Direction
import com.app.domain.model.LatLngLiteral
import com.app.domain.model.common.ApiState
import com.app.domain.repository.MapRepository
import com.app.domain.usecase.direction.GetDirectionUseCase
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.base.BaseViewModel2
import com.app.seoullo_new.view.util.DialogState
import com.app.seoullo_new.view.util.LocationService
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    private val directionUseCase: GetDirectionUseCase,
    private val locationService: LocationService,
    private val mapRepository: MapRepository
) : BaseViewModel2(dispatcherProvider) {
    // 이전 화면(PlacesDetail)에서 가져온 목적지 위/경도
    private val json: String = checkNotNull(savedStateHandle["latlng"])
    private val latLng: LatLngLiteral by lazy { Json.decodeFromString<LatLngLiteral>(json) }

    // 목적지 선택 팝업
    private val _dialogState = MutableStateFlow(DialogState())
    val dialogState: StateFlow<DialogState> = _dialogState.asStateFlow()

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    private val _direction = MutableStateFlow<ApiState<Direction>>(ApiState.Initial())
    val direction = _direction.asStateFlow()

    init {
        Logging.e("이전 화면 위/경도: ${mapRepository.setLatLng(latLng)}")
        getCurrentLocation()
    }

    fun openDirectionSelectDialog() = _dialogState.update { it.copy(isDirectionSelectDialogOpen = true) }
    fun closeDirectionSelectDialog() = _dialogState.update { it.copy(isDirectionSelectDialogOpen = false) }

    fun getCurrentLocation() {
        viewModelScope.launch {
            locationService.getCurrentLocation()
                .collect { latLng ->
                    latLng?.let {
                        _currentLocation.value = it
                    }
                }
        }
    }

    fun getDirection(
        destination: LatLngLiteral,
        starting: LatLngLiteral,
        languageCode: String
    ) {
        onIO {
            directionUseCase(
                destination = mapRepository.setLatLng(destination),
                starting = mapRepository.setLatLng(starting),
                languageCode = languageCode,
                apiKey = BuildConfig.SEOULLO_GOOGLE_MAPS_API_KEY
            ).flowOn(Dispatchers.IO)
                .collect { state ->
                    _direction.value = state
                }
        }
    }
}