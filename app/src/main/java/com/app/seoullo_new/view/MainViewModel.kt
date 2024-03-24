package com.app.seoullo_new.view

import android.Manifest
import com.app.seoullo_new.base.BaseViewModel2
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.CheckingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val checkingManager: CheckingManager
) : BaseViewModel2(dispatcherProvider) {

    init {
        checkPermission()
    }

    fun checkPermission() {
        onIO {
            checkingManager.checkPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }
}