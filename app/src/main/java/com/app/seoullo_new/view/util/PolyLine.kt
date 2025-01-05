package com.app.seoullo_new.view.util

import com.google.android.gms.maps.model.LatLng

data class PolyLine(
    val transitType: String = "",
    val transitColor: String = "",
    val polyLine: List<LatLng> = emptyList()
)
