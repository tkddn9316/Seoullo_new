package com.app.seoullo_new.base

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.seoullo_new.utils.Logging
import com.google.android.gms.location.FusedLocationProviderClient

/**
 * 기본 형태 BaseViewModel
 */
open class BaseViewModel : ViewModel() {
    /** AppBar Title */
    val title = MutableLiveData("")

    /** AppBar BackButton */
    val back = MutableLiveData(true)

    /** AppBar RefreshButton */
    val refresh = MutableLiveData(false)
    val menu = MutableLiveData(false)
    val menuClickedPosition = MutableLiveData(0)
    val loading = MutableLiveData(false)

    /** My LAT(Y)/LNG(X) */
    private val _lat = MutableLiveData("")
    val lat: LiveData<String> = _lat
    private val _lng = MutableLiveData("")
    val lng: LiveData<String> = _lng

    /** 현재 위치 위도, 경도 가져오기 */
    @SuppressLint("MissingPermission")
    fun getMyLocation(fusedLocationProviderClient: FusedLocationProviderClient, next: (() -> Unit)) {
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { success: Location? ->
                success?.let { location ->
                    _lat.value = location.latitude.toString()
                    _lng.value = location.longitude.toString()
                    next.invoke()
                }
            }
            .addOnFailureListener {
                it.message?.let { it1 -> Logging.e(it1) }
            }
    }
}