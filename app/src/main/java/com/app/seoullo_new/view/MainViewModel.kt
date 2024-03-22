package com.app.seoullo_new.view

import android.Manifest
import com.app.seoullo_new.base.BaseViewModel
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.CheckingManager
import com.app.seoullo_new.utils.Logging
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val checkingManager: CheckingManager
) : BaseViewModel(dispatcherProvider) {

    init {
        checkPermission()
    }

    private fun checkPermission() {
        onMain {
            checkingManager.checkPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }
}