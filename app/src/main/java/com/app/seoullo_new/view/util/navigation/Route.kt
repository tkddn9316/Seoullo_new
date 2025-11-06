package com.app.seoullo_new.view.util.navigation

object Route {
    const val SPLASH = "splash"

    const val MAIN = "main/{weather}/{banner}"

    const val HOME = "home"
    const val TRAVEL = "travel"
    const val COMMUNITY = "community"
    const val SETTING = "setting"

    const val TRAVEL_ROUTE = "travel_route"
    const val PLACE_LIST = "place_list/{item}"
    const val PLACE_DETAIL = "place_detail/{place}"
    const val PLACE_DETAIL_NEARBY = "place_detail_nearby/{place}"
    const val DIRECTION = "direction/{latlng}"

//    const val ADD_POST = "add"
    const val ADD_OR_EDIT_POST = "post?postId={postId}"
    const val DETAIL_POST = "detail_post/{postId}"

    const val LICENSE = "license"

    fun placeListParameter(item: String): String {
        return "place_list/$item"
    }

    fun addPost() = "post"
    fun editPost(postId: String) = "post?postId=$postId"
}