package com.app.domain.repository

import com.app.domain.model.LatLngLiteral

class MapRepository {

    // Direction API에 필요한 LatLng 데이터 가공
    fun setLatLng(
        latLngLiteral: LatLngLiteral
    ): String = "${latLngLiteral.lat},${latLngLiteral.lng}"

    fun setLatLng(
        latitude: Double,
        longitude: Double
    ): String = "${latitude},${longitude}"
}