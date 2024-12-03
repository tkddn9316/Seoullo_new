package com.app.seoullo_new.view.placeList

import android.Manifest
import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.domain.model.Places
import com.app.domain.model.PlacesNearbyRequest
import com.app.domain.usecase.places.GetPlacesListUseCase
import com.app.domain.usecase.places.GetPlacesNearbyListUseCase
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.CheckingManager
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
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class PlacesListViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val getPlacesListUseCase: GetPlacesListUseCase,
    private val getPlacesNearbyListUseCase: GetPlacesNearbyListUseCase,
    private val checkingManager: CheckingManager
) : BaseViewModel2(dispatcherProvider) {
    private val _placesListResult = MutableStateFlow<List<Places>>(emptyList())
    val placesListResult = _placesListResult.asStateFlow()

    private val _placesListResult2 = MutableStateFlow<PagingData<Places>>(PagingData.empty())
    val placesListResult2 = _placesListResult2.asStateFlow()

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

    fun getPlacesList(travelItem: TravelJsonItemData) {
        onIO {
            getPlacesListUseCase(
                BuildConfig.TOUR_API_KEY,
                travelItem.id,
                travelItem.cat
            ).flowOn(Dispatchers.IO)
                .cachedIn(viewModelScope)
                .catch { Logging.e(it.message ?: "") }
                .collect {
                    clearData()
                    _placesListResult2.value = it

                    Logging.e(loading.value!!)
                    withContext(Dispatchers.Main) { loading.value = false }
                }
        }
    }

    /** 위치 기반 리스트 검색(1Km) */
    // TODO: 매번 같은 이미지 안불러오도록 이미지 캐싱 필요
    fun getPlacesNearbyList(
        travelItem: TravelJsonItemData,
        languageCode: String
    ) {
        onIO {
            val item = travelItem.type.split("|")
            Logging.e(lat.value!!)
            Logging.e(lng.value!!)
            getPlacesNearbyListUseCase(
                BuildConfig.SEOULLO_GOOGLE_MAPS_API_KEY,
                PlacesNearbyRequest(
                    item,
                    20,
                    languageCode,
                    PlacesNearbyRequest.LocationRestriction(
                        PlacesNearbyRequest.Circle(
                            PlacesNearbyRequest.Center(
                                lat.value!!,
                                lng.value!!
                            ),
                            radius = 1000.0
                        )
                    )
                )
            )
                .flowOn(Dispatchers.IO)
                .catch { Logging.e(it.message ?: "") }
                .collect { test ->
                    clearData()
                    test.forEach { Logging.e(it.toString()) }
                    _placesListResult.value = test
                }
        }
    }

    fun getFakePlacesNearbyList(context: Context) {
        // Debug Fake Data
        val jsonString = context.assets.open("fake_nearby_places_data.json").bufferedReader().use { it.readText() }
        val fakePlaces = Json.decodeFromString<List<Places>>(jsonString)
        _placesListResult.value = fakePlaces
    }

    private fun clearData() {
        _placesListResult.value = emptyList()
        _placesListResult2.value = PagingData.empty()
    }
}