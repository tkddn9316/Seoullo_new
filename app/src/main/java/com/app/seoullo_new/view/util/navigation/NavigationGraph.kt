package com.app.seoullo_new.view.util.navigation

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.app.domain.model.Places
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.main.MainScreen
import com.app.seoullo_new.view.main.setting.LicenseScreen
import com.app.seoullo_new.view.placeDetail.PlaceDetailNearbyScreen
import com.app.seoullo_new.view.placeDetail.PlaceDetailScreen
import com.app.seoullo_new.view.placeList.PlacesListScreen
import com.app.seoullo_new.view.util.TravelJsonItemData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = Route.MAIN
    ) {
        mainScreenNavigation(navController)
        settingScreenNavigation(navController)
        travelScreenNavigation(navController)
    }
}

fun NavGraphBuilder.mainScreenNavigation(navController: NavHostController) {
    composable(Route.MAIN) {
        MainScreen(
            travelOnClick = { travelItem ->
                val itemJson = Json.encodeToString(travelItem)
                navController.navigate(Route.placeListParameter(itemJson))
            },
            settingOnClick = { route ->
                when (route) {
                    Route.LICENSE -> {
                        navController.navigate(Route.LICENSE)
                    }
                }
            }
        )
    }
}

fun NavGraphBuilder.travelScreenNavigation(navController: NavHostController) {
    composable(
        Route.PLACE_LIST,
        arguments = listOf(navArgument("item") { type = NavType.StringType })
    ) { backStackEntry ->
        val itemJson = backStackEntry.arguments?.getString("item")
        val travelItem = itemJson?.let { Json.decodeFromString<TravelJsonItemData>(it) }

        PlacesListScreen(
            travelItem = travelItem ?: TravelJsonItemData(),
            onNavigationClick = { navController.navigateUp() },
            onNearbyItemClick = {
                navController.navigate(
                    Route.PLACE_DETAIL_NEARBY
                        .replace(oldValue = "{place}", newValue = it)
                )
            },
            onItemClick = {
                navController.navigate(
                    Route.PLACE_DETAIL
                        .replace(oldValue = "{place}", newValue = it)
                )
            }
        )
    }

    composable(
        Route.PLACE_DETAIL_NEARBY,
        arguments = listOf(navArgument("place") { type = NavType.StringType })
    ) {
        PlaceDetailNearbyScreen(
            onNavigationClick = { navController.navigateUp() }
        )
    }

    composable(
        Route.PLACE_DETAIL,
        arguments = listOf(navArgument("place") { type = NavType.StringType })
    ) {
        PlaceDetailScreen(
            onNavigationClick = { navController.navigateUp() }
        )
    }
}

fun NavGraphBuilder.settingScreenNavigation(navController: NavHostController) {
    composable(Route.LICENSE) {
        LicenseScreen(onNavigationClick = { navController.navigateUp() })
    }
}