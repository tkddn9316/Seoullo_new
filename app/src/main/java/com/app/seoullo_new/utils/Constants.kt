package com.app.seoullo_new.utils

import com.app.seoullo_new.R

/**
 * presentation 계층에서 사용하는 상수 및 enum class 모음
 */
object Constants {
    const val INTENT_DATA = "_data"
    const val SELECTED_TOUR_LIST = 0
    const val SELECTED_NEARBY_LIST = 1

    enum class ContentTypeId(val id: String) {
        LEISURE_SPORTS("75"), TOURIST("76"), TRAFFIC("77"), CULTURE("78"),
        SHOPPING("79"), ACCOMMODATION("80"), RESTAURANT("82"), FESTIVAL("85")
    }

    enum class ContentType(val type: String) {
        // TODO: https://developers.google.com/maps/documentation/places/web-service/place-types?hl=ko#lodging
        // 1줄 마다 세트임
        LEISURE_SPORTS("75"),
        TOURIST("76"),
        TRAFFIC("77"),
        CULTURE("78"),
        DEPARTMENT_STORE("department_store"), MARKET("market"), CLOTHING_STORE("clothing_store"),
        HOTEL("hotel"), MOTEL("motel"), GUEST_HOUSE("guest_house"),
        RESTAURANT("restaurant"), CAFE("cafe"),
        FESTIVAL("85")
    }
}