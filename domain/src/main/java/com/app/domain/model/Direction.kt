package com.app.domain.model

import com.app.domain.model.common.BaseModel

data class Direction(
    val status: String,
    val routes: List<Route>
) : BaseModel() {
    data class Route(
        val northEastLat: Double,
        val northEastLng: Double,
        val southWestLat: Double,
        val southWestLng: Double,
        val legs: List<Leg>,
        val overviewPolyline: String,
        val summary: String,
        val warnings: String
    ) {
        data class Leg(
            val arrivalTime: String,
            val departureTime: String,
            val distance: String,
            val duration: String,
            val endAddress: String,
            val endLocation: LatLngLiteral,
            val startAddress: String,
            val startLocation: LatLngLiteral,
            val steps: List<Step>
        ) {
            data class LatLngLiteral(
                val lat: Double,
                val lng: Double
            )

            data class Step(
                val distance: String,
                val duration: String,
                val endLocation: LatLngLiteral,
                val startLocation: LatLngLiteral,
                val instructions: String,
                val polyline: String,
                val travelMode: String,
                val transitDetails: TransitDetails?,
                val steps: List<Step>?
            ) {
                data class LatLngLiteral(
                    val lat: Double,
                    val lng: Double
                )

                data class TransitDetails(
                    val arrivalStopName: String,
                    val arrivalStopTime: String,
                    val arrivalStopLocation: LatLngLiteral,
                    val departureStopName: String,
                    val departureStopTime: String,
                    val departureStopLocation: LatLngLiteral,
                    val transitAgenciesName: String,
                    val transitColor: String,
                    val transitName: String,
                    val transitTextColor: String,
                    val transitIcon: String
                ) {
                    data class LatLngLiteral(
                        val lat: Double,
                        val lng: Double
                    )
                }
            }
        }
    }
}
