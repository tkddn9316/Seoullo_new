package com.app.seoullo_new.view.util.navigation

object Route {
    const val SPLASH = "splash"

    const val MAIN = "main/{weather}/{banner}"

    const val HOME = "home"
    const val TRAVEL = "travel"
    const val SETTING = "setting"

    const val TRAVEL_ROUTE = "travel_route"
    const val PLACE_LIST = "place_list/{item}"
    const val PLACE_DETAIL = "place_detail/{place}"
    const val PLACE_DETAIL_NEARBY = "place_detail_nearby/{place}"
    const val DIRECTION = "direction/{latlng}"

    const val LICENSE = "license"

    fun placeListParameter(item: String): String {
        return "place_list/$item"
    }
}