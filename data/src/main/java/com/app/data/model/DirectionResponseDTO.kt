package com.app.data.model

import com.google.gson.annotations.SerializedName

data class DirectionResponseDTO(
    /** 출발지, 목적지, 경유지의 지오코딩에 대한 세부정보가 포함된 배열을 포함합니다.
     * geocoded_waypoints 배열의 요소는 0부터 시작하는 위치를 기준으로 출발지, 지정된 순서의 경유지 및 목적지에 해당합니다.
     * 서비스가 결과를 반환하지 않는 경우 텍스트 위도/경도 값으로 지정된 경유지에는 이러한 세부 정보가 표시되지 않습니다.
     * 이는 경로를 찾은 후 해당 경유지의 대표 주소를 얻기 위해 역지오코딩만 수행되기 때문입니다.
     * 빈 JSON 객체는 geocoded_waypoints 배열에서 해당 위치를 차지합니다. */
    @SerializedName("geocoded_waypoints")
    val geocodedWaypoints: List<GeocodedWaypoint>,
    /** 출발지에서 목적지까지의 경로 배열을 포함합니다. 경로는 중첩된 Legs와 Steps로 구성됩니다. */
    @SerializedName("routes")
    val routes: List<Route>,
    /** 요청 상태가 포함되어 있으며, 요청이 실패한 이유를 추적하는데 도움이 되는 디버깅 정보가 포함될 수 있습니다. */
    @SerializedName("status")
    val status: String
) {
    data class GeocodedWaypoint(
        /** 지오코딩 작업으로 인한 상태 코드를 나타냅니다. 이 필드에는 다음 값이 포함될 수 있습니다.
         * 허용되는 값은 다음과 같습니다: OK, ZERO_RESULTS */
        @SerializedName("geocoder_status")
        val geocoderStatus: String,
        /** 다른 Google API와 함께 사용할 수 있는 고유 식별자입니다. */
        @SerializedName("place_id")
        val placeId: String,
        /** 길찾기 계산에 사용되는 지오코딩 결과의 주소 유형을 나타냅니다. */
        @SerializedName("types")
        val types: List<String>
    )

    data class Route(
        /** overview_polyline의 뷰포트 경계 상자를 포함합니다. */
        @SerializedName("bounds")
        val bounds: Bounds,
        /** 이 경로에 대해 표시될 저작권 표시가 포함되어 있습니다. */
        @SerializedName("copyrights")
        val copyrights: String,
        /** 주어진 경로 내의 두 위치 사이의 경로 구간에 대한 정보를 포함하는 배열입니다.
         * 지정된 각 경유지 또는 목적지에 대해 별도의 구간이 존재합니다. (웨이포인트가 없는 경로에는 Leg 배열 내에 정확히 하나의 Leg가 포함됩니다.)
         * 각 Leg는 일련의 단계로 구성됩니다. */
        @SerializedName("legs")
        val legs: List<Leg>,
        /** 경로의 인코딩된 폴리라인 표현을 보유하는 객체를 포함합니다. 이 폴리라인은 결과 방향의 대략적인 경로입니다. */
        @SerializedName("overview_polyline")
        val overviewPolyline: OverviewPolyline,
        /** 경로에 대한 짧은 텍스트 설명이 포함되어 있으며 경로 이름을 지정하고 대안과 경로를 명확하게 구분하는 데 적합합니다. */
        @SerializedName("summary")
        val summary: String,
        /** 이러한 길찾기를 표시할 때 표시될 경고 배열이 포함되어 있습니다. 이러한 경고는 사용자가 직접 처리하고 표시해야 합니다. */
        @SerializedName("warnings")
        val warnings: List<String>?,
        /** 계산된 경로의 경유지 순서를 나타내는 배열입니다. */
//        @SerializedName("waypoint_order")
//        val waypointOrder: List<Any>
    ) {
        /** 남서쪽과 북동쪽 모서리 지점을 기준으로 한 지리적 좌표의 직사각형입니다. */
        data class Bounds(
            @SerializedName("northeast")
            val northeast: LatLngLiteral,
            @SerializedName("southwest")
            val southWest: LatLngLiteral
        ) {
            data class LatLngLiteral(
                @SerializedName("lat")
                val lat: Double,
                @SerializedName("lng")
                val lng: Double
            )
        }

        data class Leg(
            /** 이 구간의 예상 도착 시간이 포함되어 있습니다. 이 속성은 대중교통 경로에 대해서만 반환됩니다. */
            @SerializedName("arrival_time")
            val arrivalTime: TimeZoneTextValueObject,
            /** Time 객체로 지정된 이 구간의 예상 출발 시간을 포함합니다. 출발_시간은 대중교통 길찾기에만 사용할 수 있습니다. */
            @SerializedName("departure_time")
            val departureTime: TimeZoneTextValueObject,
            /** 이 Leg로 이동한 총 거리입니다. */
            @SerializedName("distance")
            val distance: TextValueObject,
            /** 이 구간의 총 지속 시간입니다. */
            @SerializedName("duration")
            val duration: TextValueObject,
            /**이 구간의 end_location을 역지오코딩하여 사람이 읽을 수 있는 주소(일반적으로 거리 주소)를 포함합니다.
             * 이 콘텐츠는 있는 그대로 읽어야 합니다. */
            @SerializedName("end_address")
            val endAddress: String,
            /** 이 구간의 지정된 목적지의 위도/경도 좌표입니다.
             * Directions API는 시작점과 끝점에서 가장 가까운 교통 옵션(일반적으로 도로)을 사용하여 위치 간 길찾기를 계산하므로,
             * 예를 들어 도로가 목적지 근처에 있지 않은 경우 end_location은 이 구간에 제공된 목적지와 다를 수 있습니다. */
            @SerializedName("end_location")
            val endLocation: LatLngLiteral,
            /** 이 구간의 start_location을 역지오코딩하여 얻은 사람이 읽을 수 있는 주소(일반적으로 거리 주소)를 포함합니다.
             * 이 콘텐츠는 있는 그대로 읽어야 합니다. */
            @SerializedName("start_address")
            val startAddress: String,
            /** 이 다리 원점의 위도/경도 좌표입니다.
             * Directions API는 시작점과 끝점에서 가장 가까운 교통 옵션(일반적으로 도로)을 사용하여 위치 간 길찾기를 계산하므로,
             * 예를 들어 도로가 출발지 근처에 있지 않은 경우 start_location은 이 구간에 제공된 출발지와 다를 수 있습니다. */
            @SerializedName("start_location")
            val startLocation: LatLngLiteral,
            /** 여행 구간의 각 단계에 대한 정보를 나타내는 일련의 단계입니다. */
            @SerializedName("steps")
            val steps: List<Step>?,
            /** leg에 따른 교통 속도에 대한 정보 */
//            @SerializedName("traffic_speed_entry")
//            val trafficSpeedEntry: List<Any>,
            /** 이 leg를 따라 경유 웨이포인트의 위치입니다. */
//            @SerializedName("via_waypoint")
//            val viaWaypoint: List<Any>
        ) {
            data class TimeZoneTextValueObject(
                @SerializedName("text")
                val text: String,
                @SerializedName("time_zone")
                val timeZone: String,
                @SerializedName("value")
                val value: Int
            )

            data class TextValueObject(
                @SerializedName("text")
                val text: String,
                @SerializedName("value")
                val value: Int
            )

            data class LatLngLiteral(
                @SerializedName("lat")
                val lat: Double,
                @SerializedName("lng")
                val lng: Double
            )

            data class Step(
                /** 다음 단계까지 이 단계에서 수행된 거리를 포함합니다. 거리를 알 수 없는 경우 이 필드는 정의되지 않을 수 있습니다. */
                @SerializedName("distance")
                val distance: TextValueObject,
                /** 다음 단계까지 단계를 수행하는 데 필요한 일반적인 시간을 포함합니다. 기간을 알 수 없는 경우 이 필드는 정의되지 않을 수 있습니다. */
                @SerializedName("duration")
                val duration: TextValueObject,
                /** 이 단계의 마지막 지점 위치를 포함합니다. */
                @SerializedName("end_location")
                val endLocation: LatLngLiteral,
                /** HTML 텍스트 문자열로 표시되는 이 단계에 대한 형식화된 지침이 포함되어 있습니다.
                 * 이 콘텐츠는 있는 그대로 읽어야 합니다. */
                @SerializedName("html_instructions")
                val htmlInstructions: String?,
                /** 단계의 인코딩된 폴리라인 표현을 보유하는 단일 포인트 객체를 포함합니다.
                 * 이 폴리라인은 계단의 대략적인 경로입니다. */
                @SerializedName("polyline")
                val polyline: Polyline,
                /** 이 단계의 시작점 위치를 포함합니다. */
                @SerializedName("start_location")
                val startLocation: LatLngLiteral,
                /** 대중교통 길찾기에서 걷기 또는 운전 단계에 대한 자세한 길찾기가 포함되어 있습니다.
                 * 하위 단계는 travel_mode가 'transit'로 설정된 경우에만 사용할 수 있습니다.
                 * 내부 steps 배열은 steps와 동일한 유형입니다. */
                @SerializedName("steps")
                val steps: List<Step>?,
                /** 이동 모드가 대중교통인 경우 이 단계와 관련된 세부정보입니다. */
                @SerializedName("transit_details")
                val transitDetails: TransitDetails?,
                /** 사용된 travelMode 유형을 포함합니다. */
                @SerializedName("travel_mode")
                val travelMode: String
            ) {
                data class TextValueObject(
                    @SerializedName("text")
                    val text: String,
                    @SerializedName("value")
                    val value: Int
                )

                data class LatLngLiteral(
                    @SerializedName("lat")
                    val lat: Double,
                    @SerializedName("lng")
                    val lng: Double
                )

                data class Polyline(
                    @SerializedName("points")
                    val points: String
                )

                data class TransitDetails(
                    /** 도착 환승 정류장입니다. */
                    @SerializedName("arrival_stop")
                    val arrivalStop: DirectionsTransitStop,
                    @SerializedName("arrival_time")
                    val arrivalTime: TimeZoneTextValueObject,
                    /** 출발 환승 정류장입니다. */
                    @SerializedName("departure_stop")
                    val departureStop: DirectionsTransitStop,
                    @SerializedName("departure_time")
                    val departureTime: TimeZoneTextValueObject,
                    /** 차량이나 출발 정류장에 표시된 대로 이 노선에서 이동할 방향을 지정합니다.
                     * 이것은 종종 종착역이 될 것입니다. */
                    @SerializedName("headsign")
                    val headSign: String,
                    /** 현재 동일한 정류장에서 출발하는 예상 간격(초)을 지정합니다.
                     * 예를 들어, 배차간격 값이 600이면 버스를 놓칠 경우 10분 정도 기다려야 합니다. */
                    @SerializedName("headway")
                    val headway: Int,
                    /** 이 단계에서 사용되는 대중교통 노선에 대한 정보가 포함되어 있습니다. */
                    @SerializedName("line")
                    val line: Line,
                    /** 출발부터 도착 정류장까지의 정류장 수입니다. 여기에는 도착 정류장이 포함되지만 출발 정류장은 포함되지 않습니다.
                     * 예를 들어 길찾기가 정류장 A에서 출발하여 정류장 B와 C를 통과하고 정류장 D에 도착하는 경우 num_stops는 3을 반환합니다. */
                    @SerializedName("num_stops")
                    val numStops: Int
                ) {
                    data class DirectionsTransitStop(
                        @SerializedName("location")
                        val location: Location,
                        @SerializedName("name")
                        val name: String
                    ) {
                        data class Location(
                            @SerializedName("lat")
                            val lat: Double,
                            @SerializedName("lng")
                            val lng: Double
                        )
                    }

                    data class TimeZoneTextValueObject(
                        @SerializedName("text")
                        val text: String,
                        @SerializedName("time_zone")
                        val timeZone: String,
                        @SerializedName("value")
                        val value: Int
                    )

                    data class Line(
                        /** 이 대중교통 노선을 운영하는 대중교통 기관입니다. */
                        @SerializedName("agencies")
                        val agencies: List<Agency>,
                        /** 이 라인의 간판에 일반적으로 사용되는 색상입니다. */
                        @SerializedName("color")
                        val color: String,
                        /** 이 대중교통 노선의 전체 이름입니다. */
                        @SerializedName("name")
                        val name: String,
                        /** 이 대중교통 노선의 짧은 이름입니다.
                         * 이는 일반적으로 "M7" 또는 "355"와 같은 줄 번호입니다. */
                        @SerializedName("short_name")
                        val shortName: String,
                        /** 이 라인의 간판에 일반적으로 사용되는 색상입니다. */
                        @SerializedName("text_color")
                        val textColor: String,
                        /** 이 대중교통 노선에서 운행되는 차량 유형입니다. */
                        @SerializedName("vehicle")
                        val vehicle: Vehicle
                    ) {
                        data class Agency(
                            /** 이 대중교통 기관의 이름입니다. */
                            @SerializedName("name")
                            val name: String,
                            /** 대중교통 기관의 URL입니다. */
                            @SerializedName("url")
                            val url: String
                        )

                        data class Vehicle(
                            /** 이 차량 유형과 관련된 아이콘의 URL을 포함합니다. */
                            @SerializedName("icon")
                            val icon: String,
                            /** 이 차량의 이름은 대문자로 표시됩니다. */
                            @SerializedName("name")
                            val name: String,
                            /** 사용된 차량의 종류. */
                            @SerializedName("type")
                            val type: String
                        )
                    }
                }
            }
        }

        data class OverviewPolyline(
            @SerializedName("points")
            val points: String
        )
    }
}