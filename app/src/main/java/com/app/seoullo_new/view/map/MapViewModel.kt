package com.app.seoullo_new.view.map

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.domain.model.Direction
import com.app.domain.model.DirectionRequest
import com.app.domain.model.ReverseGeocoding
import com.app.domain.model.common.ApiState
import com.app.domain.repository.MapRepository
import com.app.domain.usecase.direction.GetDirectionUseCase
import com.app.domain.usecase.direction.GetReverseGeocodingUseCase
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.LocationService
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.base.BaseViewModel2
import com.app.seoullo_new.view.util.DialogState
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    private val reverseGeocodingUseCase: GetReverseGeocodingUseCase,
    private val directionUseCase: GetDirectionUseCase,
    private val locationService: LocationService,
    private val mapRepository: MapRepository
) : BaseViewModel2(dispatcherProvider) {
    // 이전 화면(PlacesDetail)에서 가져온 목적지 위/경도
    private val json: String = checkNotNull(savedStateHandle["latlng"])
    val latLng: DirectionRequest by lazy { Json.decodeFromString<DirectionRequest>(json) }

    // 목적지 선택 팝업
    private val _dialogState = MutableStateFlow(DialogState())
    val dialogState: StateFlow<DialogState> = _dialogState.asStateFlow()

    // 현재 위치
    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    // 역 지오코딩 결과
    private val _currentAddress = MutableStateFlow<ApiState<ReverseGeocoding>>(ApiState.Initial())
    val currentAddress = _currentAddress.asStateFlow()

    // 길 찾기 결과
    private val _direction = MutableStateFlow<ApiState<Direction>>(ApiState.Initial())
    val direction = _direction.asStateFlow()

    init {
        Logging.e("이전 화면: $latLng")
        getCurrentLocation()
    }

    fun openDirectionSelectDialog() = _dialogState.update { it.copy(isDirectionSelectDialogOpen = true) }
    fun closeDirectionSelectDialog() = _dialogState.update { it.copy(isDirectionSelectDialogOpen = false) }

    fun openDirectionBottomSheet() = _dialogState.update { it.copy(isDirectionBottomSheetOpen = true) }
    fun closeDirectionBottomSheet() = _dialogState.update { it.copy(isDirectionBottomSheetOpen = false) }

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

    fun getCurrentLocationAddress(
        languageCode: String
    ) {
        _currentLocation.value?.let {
            val currentLatLng = DirectionRequest(
                lat = it.latitude,
                lng = it.longitude
            )
            onIO {
                reverseGeocodingUseCase(
                    latLng = mapRepository.setLatLng(currentLatLng),
                    languageCode = languageCode,
                    apiKey = BuildConfig.SEOULLO_GOOGLE_MAPS_API_KEY
                ).collect { state ->
                    _currentAddress.value = state
                }
            }
        }
    }

    fun getDirection(
        destination: DirectionRequest,
        starting: DirectionRequest,
        languageCode: String
    ) {
        onIO {
            directionUseCase(
                destination = mapRepository.onDirectionRequestProcess(destination),
                starting = mapRepository.onDirectionRequestProcess(starting),
                languageCode = languageCode,
                apiKey = BuildConfig.SEOULLO_GOOGLE_MAPS_API_KEY
            ).flowOn(Dispatchers.IO)
                .collect { state ->
                    _direction.value = state
                }
        }
    }

    /** Debug Fake Data */
    fun getFakeDirection(context: Context) {
        viewModelScope.launch {
            _direction.value = ApiState.Loading()

            delay(1000)

            val jsonString = context.assets.open("example_google_direction2.json").bufferedReader().use { it.readText() }
            val fakePlaces = Json.decodeFromString<Direction>(jsonString)
            _direction.value = ApiState.Success(fakePlaces)
        }
    }

    fun handleDirectionSuccess() {
        _direction.value = ApiState.Initial()
    }
}