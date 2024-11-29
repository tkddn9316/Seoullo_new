package com.app.seoullo_new.view.util.navigation

object Route {
    const val MAIN = "main"

    const val HOME = "home"
    const val TRAVEL = "travel"
    const val SETTING = "setting"

    const val PLACE_LIST = "place_list/{item}"

    const val LICENSE = "license"

    fun placeListParameter(item: String): String {
        return "place_list/$item"
    }
}