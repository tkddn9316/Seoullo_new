package com.app.domain.model

import com.app.domain.model.common.BaseModel
import kotlinx.serialization.Serializable

@Serializable
data class Direction(
    val status: String = "",
    val routes: List<Route> = emptyList()
) : BaseModel() {

    @Serializable
    data class Route(
        val northEastLat: Double = 0.0,
        val northEastLng: Double = 0.0,
        val southWestLat: Double = 0.0,
        val southWestLng: Double = 0.0,
        val legs: List<Leg> = emptyList(),
        val overviewPolyline: String = "",
        val summary: String = "",
        val warnings: String = ""
    ) {
        @Serializable
        data class Leg(
            val arrivalTime: String = "",
            val departureTime: String = "",
            val distance: String = "",
            val duration: String = "",
            val endAddress: String = "",
            val endLocation: LatLngLiteral = LatLngLiteral(),
            val startAddress: String = "",
            val startLocation: LatLngLiteral = LatLngLiteral(),
            val steps: List<Step> = emptyList()
        ) {
            @Serializable
            data class LatLngLiteral(
                val lat: Double = 0.0,
                val lng: Double = 0.0
            )

            @Serializable
            data class Step(
                val distance: String = "",
                val duration: String = "",
                val endLocation: LatLngLiteral = LatLngLiteral(),
                val startLocation: LatLngLiteral = LatLngLiteral(),
                val instructions: String = "",
                val polyline: String = "",
                val travelMode: String = "",
                val transitDetails: TransitDetails? = null,
                val steps: List<Step>? = null
            ) {
                @Serializable
                data class LatLngLiteral(
                    val lat: Double = 0.0,
                    val lng: Double = 0.0
                )

                @Serializable
                data class TransitDetails(
                    val arrivalStopName: String = "",
                    val arrivalStopTime: String = "",
                    val arrivalStopLocation: LatLngLiteral = LatLngLiteral(),
                    val departureStopName: String = "",
                    val departureStopTime: String = "",
                    val departureStopLocation: LatLngLiteral = LatLngLiteral(),
                    val transitAgenciesName: String = "",
                    val transitColor: String = "",
                    val transitName: String = "",
                    val transitTextColor: String = "",
                    val transitIcon: String = ""
                ) {
                    @Serializable
                    data class LatLngLiteral(
                        val lat: Double = 0.0,
                        val lng: Double = 0.0
                    )
                }
            }
        }
    }
}
