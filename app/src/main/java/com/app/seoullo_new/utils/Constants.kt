package com.app.seoullo_new.utils

/**
 * presentation 계층에서 사용하는 상수 및 enum class 모음
 */
object Constants {

    enum class ContentTypeId(val type: String) {
        LEISURE_SPORTS("75"), TOURIST("76"), TRAFFIC("77"), CULTURE("78"),
        SHOPPING("79"), ACCOMMODATION("80"), RESTAURANT("82"), FESTIVAL("85")
    }
}