package com.app.seoullo_new.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.seoullo_new.utils.Route
import com.app.seoullo_new.view.main.Main
import com.app.seoullo_new.view.placeList.Setup

@Composable
fun InitNavHost(navController: NavHostController) {
//    NavHost(
//        navController = navController,
//        startDestination = Route.Route.Main.name
//    ) {
//        composable(route = Route.Route.Main.name) { Main() }
//        composable(route = Route.Route.PlacesList.name) { Setup() }
//    }
}