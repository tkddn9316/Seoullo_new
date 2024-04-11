package com.app.seoullo_new.view

import android.Manifest
import com.app.domain.model.Places
import com.app.domain.model.PlacesNearbyRequest
import com.app.domain.usecase.places.GetPlacesNearbyListUseCase
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.base.BaseViewModel2
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.CheckingManager
import com.app.seoullo_new.utils.Constants.ContentType
import com.app.seoullo_new.utils.Logging
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class PlacesListViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val getPlacesNearbyListUseCase: GetPlacesNearbyListUseCase,
    private val checkingManager: CheckingManager
) : BaseViewModel2(dispatcherProvider) {
    private val _placesListResult = MutableStateFlow<List<Places>>(emptyList())
    val placesListResult = _placesListResult.asStateFlow()

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
//                    getPlacesNearbyList()

                    // TODO: TEST DATA
                    test()
                }
            } else {
                Logging.e("권한 X")
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
                    listOf(ContentType.DEPARTMENT_STORE.type, ContentType.MARKET.type, ContentType.CLOTHING_STORE.type),
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
                    _placesListResult.value = emptyList()
                    test.forEach { Logging.e(it.toString()) }
                    _placesListResult.value = test
                }
        }
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