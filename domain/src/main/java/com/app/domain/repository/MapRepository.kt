package com.app.domain.repository

import com.app.domain.model.DirectionRequest

class MapRepository {

    // Direction API에 필요한 LatLng 데이터 가공
    fun setLatLng(
        directionRequest: DirectionRequest
    ): String = "${directionRequest.lat},${directionRequest.lng}"

    fun setLatLng(
        latitude: Double,
        longitude: Double
    ): String = "${latitude},${longitude}"

    fun onDirectionRequestProcess(directionRequest: DirectionRequest): String {
        return if (directionRequest.placeId.isNotEmpty()) "place_id:${directionRequest.placeId}"
        else if (directionRequest.lat > 0 && directionRequest.lng > 0) setLatLng(directionRequest)
        else directionRequest.address
    }
}