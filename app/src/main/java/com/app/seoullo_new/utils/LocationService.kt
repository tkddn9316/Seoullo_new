package com.app.seoullo_new.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.app.seoullo_new.utils.Util.hasLocationPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

/** 현재 위치 불러오기 */
class LocationService(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationServiceInterface {

    private val cancellationTokenSource: CancellationTokenSource = CancellationTokenSource()

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Flow<LatLng?> = flow {
        if (!context.hasLocationPermission()) {
            emit(null)
            return@flow
        }

        val currentLocationTask: Task<Location> = fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        )

        val latLng = suspendCancellableCoroutine<LatLng?> { continuation ->
            currentLocationTask.addOnCompleteListener { task : Task<Location> ->
                if (task.isSuccessful && task.result != null) {
                    val location: Location = task.result
                    continuation.resume(
                        value = LatLng(location.latitude, location.longitude)
                    ) { cause, _, _ ->
                        cancellationTokenSource.cancel()
                        throw cause
                    }
                } else {
                    val exception = task.exception
                    if (exception != null) {
                        continuation.resumeWithException(exception)
                    }
                }
            }
        }

        emit(latLng)
    }
}

//class LocationService(
//    private val context: Context,
//    private val fusedLocationClient: FusedLocationProviderClient
//) : LocationServiceInterface {
//
//    @ExperimentalCoroutinesApi
//    @SuppressLint("MissingPermission")
//    override fun getCurrentLocation(): Flow<LatLng?> = callbackFlow {
//        if (!context.hasLocationPermission()) {
//            trySend(null)
//            close()
//            return@callbackFlow
//        }
//
//        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
//            .setMinUpdateIntervalMillis(500L) // 최소 업데이트 간격
//            .build()
//
//        val locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                locationResult.lastLocation?.let { location ->
//                    trySend(LatLng(location.latitude, location.longitude))
//                }
//            }
//        }
//
//        fusedLocationClient.requestLocationUpdates(
//            locationRequest,
//            locationCallback,
//            Looper.getMainLooper()
//        )
//
//        awaitClose {
//            fusedLocationClient.removeLocationUpdates(locationCallback)
//        }
//    }
//}