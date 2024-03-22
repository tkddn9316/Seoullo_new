package com.app.seoullo_new.base

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.Logging
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel(dispatcherProvider: DispatcherProvider) : ViewModel(),
    DispatcherProvider by dispatcherProvider {

    /** AppBar Title */
    val title = MutableLiveData("")

    /** AppBar BackButton */
    val back = MutableLiveData(true)

    /** AppBar RefreshButton */
    val refresh = MutableLiveData(true)
    val loading = MutableLiveData(false)

    /** My LAT(Y)/LON(X) */
    private val _lat = MutableLiveData("")
    val lat: LiveData<String> = _lat
    private val _lng = MutableLiveData("")
    val lng: LiveData<String> = _lng

    /** viewModelScope Main Thread */
    inline fun BaseViewModel.onMain(
        crossinline body: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch {
        loading.value = true
        body(this)
        loading.value = false
    }

    /** viewModelScope IO Thread */
    inline fun BaseViewModel.onIO(
        crossinline body: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(io) {
        withContext(Dispatchers.Main) { loading.value = true }
        body(this)
        withContext(Dispatchers.Main) { loading.value = false }
    }

    /** viewModelScope Default */
    inline fun BaseViewModel.onDefault(
        crossinline body: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(default) {
        withContext(Dispatchers.Main) { loading.value = true }
        body(this)
        withContext(Dispatchers.Main) { loading.value = false }
    }

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