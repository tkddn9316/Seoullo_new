package com.app.data.mapper

import com.app.data.model.DirectionResponseDTO
import com.app.domain.model.Direction

fun directionMapper(directionResponseDTO: DirectionResponseDTO): Direction =
    Direction(
        status = directionResponseDTO.status,
        routes = directionResponseDTO.routes.map { routesDTO ->
            Direction.Route(
                northEastLat = routesDTO.bounds.northeast.lat,
                northEastLng = routesDTO.bounds.northeast.lng,
                southWestLat = routesDTO.bounds.southWest.lat,
                southWestLng = routesDTO.bounds.southWest.lng,
                legs = routesDTO.legs.map { legDTO ->
                    Direction.Route.Leg(
                        arrivalTime = legDTO.arrivalTime.text,
                        departureTime = legDTO.departureTime.text,
                        distance = legDTO.distance.text,
                        duration = legDTO.duration.text,
                        endAddress = legDTO.endAddress,
                        endLocation = Direction.Route.Leg.LatLngLiteral(
                            lat = legDTO.endLocation.lat,
                            lng = legDTO.endLocation.lng
                        ),
                        startAddress = legDTO.startAddress,
                        startLocation = Direction.Route.Leg.LatLngLiteral(
                            lat = legDTO.startLocation.lat,
                            lng = legDTO.startLocation.lng
                        ),
                        steps = legDTO.steps?.map { step ->
                            mapStep(step)
                        } ?: emptyList()
                    )
                },
                overviewPolyline = routesDTO.overviewPolyline.points,
                summary = routesDTO.summary,
                warnings = routesDTO.warnings?.firstOrNull() ?: ""
            )
        }
    )

private fun mapStep(stepDTO: DirectionResponseDTO.Route.Leg.Step): Direction.Route.Leg.Step =
    Direction.Route.Leg.Step(
        distance = stepDTO.distance.text,
        duration = stepDTO.duration.text,
        endLocation = Direction.Route.Leg.Step.LatLngLiteral(
            lat = stepDTO.endLocation.lat,
            lng = stepDTO.endLocation.lng
        ),
        startLocation = Direction.Route.Leg.Step.LatLngLiteral(
            lat = stepDTO.startLocation.lat,
            lng = stepDTO.startLocation.lng
        ),
        instructions = stepDTO.htmlInstructions ?: "",
        polyline = stepDTO.polyline.points,
        travelMode = stepDTO.travelMode,
        transitDetails = stepDTO.transitDetails?.let { transitDTO ->
            Direction.Route.Leg.Step.TransitDetails(
                arrivalStopName = transitDTO.arrivalStop.name,
                arrivalStopTime = transitDTO.arrivalTime.text,
                arrivalStopLocation = Direction.Route.Leg.Step.TransitDetails.LatLngLiteral(
                    lat = transitDTO.arrivalStop.location.lat,
                    lng = transitDTO.arrivalStop.location.lng
                ),
                departureStopName = transitDTO.departureStop.name,
                departureStopTime = transitDTO.departureTime.text,
                departureStopLocation = Direction.Route.Leg.Step.TransitDetails.LatLngLiteral(
                    lat = transitDTO.departureStop.location.lat,
                    lng = transitDTO.departureStop.location.lng
                ),
                transitAgenciesName = transitDTO.line.agencies.firstOrNull()?.name ?: "",
                transitColor = transitDTO.line.color,
                transitName = "${transitDTO.line.name} ${transitDTO.line.shortName}",
                transitTextColor = transitDTO.line.textColor,
                transitIcon = transitDTO.line.vehicle.icon
            )
        },
        steps = stepDTO.steps?.map { nestedStepDTO ->
            mapStep(nestedStepDTO)
        } ?: emptyList()
    )

