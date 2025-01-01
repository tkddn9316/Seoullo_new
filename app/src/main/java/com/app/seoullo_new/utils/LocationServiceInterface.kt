package com.app.seoullo_new.utils

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationServiceInterface {
    fun getCurrentLocation(): Flow<LatLng?>
}