package com.app.seoullo_new.view

import android.Manifest
import com.app.domain.model.TourInfo
import com.app.domain.usecase.tourInfo.GetTourInfoUseCase
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.base.BaseViewModel
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.CheckingManager
import com.app.seoullo_new.utils.Constants.ContentTypeId
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
class TourListViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val getTourInfoUseCase: GetTourInfoUseCase,
    private val checkingManager: CheckingManager
) : BaseViewModel(dispatcherProvider) {
    private val _tourInfoListResult = MutableStateFlow<List<TourInfo>>(emptyList())
    val tourInfoListResult = _tourInfoListResult.asStateFlow()

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
                    getTourInfo()
                }
            } else {
                Logging.e("권한 X")
            }
        }
    }


    private fun getTourInfo() {
        onIO {
            getTourInfoUseCase(
                BuildConfig.TOUR_API_KEY,
                ContentTypeId.RESTAURANT.type,
                lng.value!!,
                lat.value!!,
                10000
            )
                .flowOn(Dispatchers.IO)
                .catch { Logging.e(it.message!!) }
                .collect { test ->
                    test.forEach {
                        Logging.e(it.toString())
                    }
                    _tourInfoListResult.value = test
                }
        }
    }
}