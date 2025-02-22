package com.app.seoullo_new.view.placesList

import android.Manifest
import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.domain.model.Places
import com.app.domain.model.PlacesNearbyRequest
import com.app.domain.model.common.ApiState
import com.app.domain.model.theme.Language
import com.app.domain.usecase.places.GetPlacesListUseCase
import com.app.domain.usecase.places.GetPlacesNearbyListUseCase
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.CheckingManager
import com.app.seoullo_new.utils.Constants.SortCriteria
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.base.BaseViewModel2
import com.app.seoullo_new.view.util.TravelJsonItemData
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class PlacesListViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val getPlacesListUseCase: GetPlacesListUseCase,
    private val getPlacesNearbyListUseCase: GetPlacesNearbyListUseCase,
    private val checkingManager: CheckingManager
) : BaseViewModel2(dispatcherProvider) {
    private val _placesNearbyState = MutableStateFlow<ApiState<List<Places>>>(ApiState.Initial())
    val placesNearbyState = _placesNearbyState.asStateFlow()

    private val _placesListResult = MutableStateFlow<PagingData<Places>>(PagingData.empty())
    val placesListResult = _placesListResult.asStateFlow()

    private var isPlacesListLoaded = false // 로드 상태 확인 플래그

    private var lastSortCriteria: SortCriteria? = null      // 마지막 Sort 변경 기록

    fun checkPermission(fusedLocationProviderClient: FusedLocationProviderClient) {
        onMain {
            if (checkingManager.checkPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                getMyLocation(fusedLocationProviderClient) {
                    Logging.e(lat.value!!)
                    Logging.e(lng.value!!)
                }
            } else {
                Logging.e("권한 X")
            }
        }
    }

    fun getPlacesList(
        travelItem: TravelJsonItemData,
        languageCode: Language
    ) {
        if (isPlacesListLoaded) return

        onIO {
            getPlacesListUseCase(
                serviceUrl = if (languageCode == Language.ENGLISH) "EngService1" else "KorService1",
                serviceKey = BuildConfig.TOUR_API_KEY,
                contentTypeId = if (languageCode == Language.ENGLISH) travelItem.id else travelItem.id_ko,
                category = travelItem.cat
            ).flowOn(Dispatchers.IO)
                .cachedIn(viewModelScope)
                .catch { Logging.e(it.message ?: "") }
                .collect {
                    _placesListResult.value = it
                    isPlacesListLoaded = true
                }
        }
    }

    /** 위치 기반 리스트 검색(1Km) */
    fun getPlacesNearbyList(
        travelItem: TravelJsonItemData,
        languageCode: String
    ) {
        if (_placesNearbyState.value !is ApiState.Initial) return
        _placesNearbyState.value = ApiState.Loading()

        onIO {
            val item = travelItem.type.split("|")
            getPlacesNearbyListUseCase(
                apiKey = BuildConfig.SEOULLO_GOOGLE_MAPS_API_KEY,
                placesNearbyRequest = PlacesNearbyRequest(
                    item, 20, languageCode,
                    PlacesNearbyRequest.LocationRestriction(
                        PlacesNearbyRequest.Circle(
                            PlacesNearbyRequest.Center(lat.value!!, lng.value!!),
                            radius = 1000.0
                        )
                    )
                )
            )
                .flowOn(Dispatchers.IO)
                .collect { state ->
                    // 상태 업데이트
                    _placesNearbyState.value = state
                }
        }
    }

    fun sortPlaces(criteria: SortCriteria) {
        if (lastSortCriteria == criteria) return

        val state = _placesNearbyState.value
        if (state is ApiState.Success && !state.data.isNullOrEmpty()) {
            val sortedList = when (criteria) {
                SortCriteria.RATING -> state.data!!.sortedByDescending { it.rating }
                SortCriteria.REVIEW -> state.data!!.sortedByDescending { it.userRatingCount }
            }
            _placesNearbyState.value = ApiState.Success(sortedList)
            lastSortCriteria = criteria
        }
    }

    /** Debug Fake Data */
    fun getFakePlacesNearbyList(context: Context) {
        if (_placesNearbyState.value !is ApiState.Initial) return
        _placesNearbyState.value = ApiState.Loading()

        val jsonString = context.assets.open("fake_nearby_places_data.json").bufferedReader().use { it.readText() }
        val fakePlaces = Json.decodeFromString<List<Places>>(jsonString)
        _placesNearbyState.value = ApiState.Success(fakePlaces)
    }
}