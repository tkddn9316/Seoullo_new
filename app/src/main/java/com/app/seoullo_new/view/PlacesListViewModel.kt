package com.app.seoullo_new.view

import android.Manifest
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.domain.model.Places
import com.app.domain.model.PlacesNearbyRequest
import com.app.domain.usecase.places.GetPlacesListUseCase
import com.app.domain.usecase.places.GetPlacesNearbyListUseCase
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.base.BaseViewModel2
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.CheckingManager
import com.app.seoullo_new.utils.Constants.ContentType
import com.app.seoullo_new.utils.Constants.ContentTypeId
import com.app.seoullo_new.utils.Logging
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
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

    init {
        title.value = "TEST"
        menu.value = true
    }

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
                    getPlacesList()
//                    getPlacesNearbyList()

                    // TODO: TEST DATA
//                    test()
                }
            } else {
                Logging.e("권한 X")
            }
        }
    }

    fun getPlacesList() {
        onIO {
            getPlacesListUseCase(
                BuildConfig.TOUR_API_KEY,
                ContentTypeId.RESTAURANT.id
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
    fun getPlacesNearbyList() {
        onIO {
            getPlacesNearbyListUseCase(
                BuildConfig.SEOULLO_GOOGLE_MAPS_API_KEY,
                PlacesNearbyRequest(
                    listOf(
                        ContentType.RESTAURANT.type,
                        ContentType.CAFE.type
                    ),
                    20,
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

    private fun clearData() {
        _placesListResult.value = emptyList()
        _placesListResult2.value = PagingData.empty()
    }

    fun test() {
        _placesListResult.value = listOf(
            Places(
                "places/ChIJWX6IgJaffDURpwHX788tTrI, id=ChIJWX6IgJaffDURpwHX788tTrI",
                "ChIJWX6IgJaffDURpwHX788tTrI",
                "Jungdamun Bossam",
                "주소",
                "fds",
                false,
                ""
            ),
            Places(
                "places/ChIJWX6IgJaffDURpwHX788tTrI, id=ChIJWX6IgJaffDURpwHX788tTrI",
                "ChIJWX6IgJaffDURpwHX788tTrI",
                "Jungdamun Bossam",
                "주소",
                "fds",
                false,
                ""
            ),
            Places(
                "places/ChIJWX6IgJaffDURpwHX788tTrI, id=ChIJWX6IgJaffDURpwHX788tTrI",
                "ChIJWX6IgJaffDURpwHX788tTrI",
                "Jungdamun Bossam",
                "주소",
                "fds",
                false,
                ""
            ),
            Places(
                "places/ChIJWX6IgJaffDURpwHX788tTrI, id=ChIJWX6IgJaffDURpwHX788tTrI",
                "ChIJWX6IgJaffDURpwHX788tTrI",
                "Jungdamun Bossam",
                "주소",
                "fds",
                false,
                ""
            ),
            Places(
                "places/ChIJWX6IgJaffDURpwHX788tTrI, id=ChIJWX6IgJaffDURpwHX788tTrI",
                "ChIJWX6IgJaffDURpwHX788tTrI",
                "Jungdamun Bossam",
                "주소",
                "fds",
                false,
                ""
            )
        )
    }
}