package com.app.data.utils

object Constants {
    enum class PlacesTableName(val tableName: String) {
        LEISURE_SPORTS("Leisure_Sports"), TOURIST("Tourist"), TRAFFIC("Traffic"), CULTURE("Culture"),
        SHOPPING("Shopping"), ACCOMMODATION("Accommodation"), RESTAURANT("Restaurant"), FESTIVAL("Festival")
    }

    /** TourAPI에서 제공하는 데이터 대분류 */
    enum class PlacesContentId(val id: String) {
        LEISURE_SPORTS("75"), TOURIST("76"), TRAFFIC("77"), CULTURE("78"),
        SHOPPING("79"), ACCOMMODATION("80"), RESTAURANT("Restaurant"), FESTIVAL("85")
    }
}