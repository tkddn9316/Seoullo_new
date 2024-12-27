package com.app.seoullo_new.view.map

import androidx.lifecycle.SavedStateHandle
import com.app.domain.model.Direction
import com.app.domain.model.LatLngLiteral
import com.app.domain.model.common.ApiState
import com.app.domain.repository.MapRepository
import com.app.domain.usecase.direction.GetDirectionUseCase
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.base.BaseViewModel2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    private val directionUseCase: GetDirectionUseCase,
    private val mapRepository: MapRepository
) : BaseViewModel2(dispatcherProvider) {
    private val json: String = checkNotNull(savedStateHandle["latlng"])
    private val latLng: LatLngLiteral by lazy { Json.decodeFromString<LatLngLiteral>(json) }

    private val _direction = MutableStateFlow<ApiState<Direction>>(ApiState.Initial())
    val direction = _direction.asStateFlow()

    init {
        Logging.e(mapRepository.setLatLng(latLng))
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