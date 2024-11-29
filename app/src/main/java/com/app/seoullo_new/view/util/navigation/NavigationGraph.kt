package com.app.seoullo_new.view.util.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.app.seoullo_new.view.main.MainScreen
import com.app.seoullo_new.view.main.setting.LicenseScreen
import com.app.seoullo_new.view.placeList.PlacesListScreen
import com.app.seoullo_new.view.util.TravelJsonItemData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
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
            onNavigationClick = { navController.navigateUp() }
        )
    }
}

fun NavGraphBuilder.settingScreenNavigation(navController: NavHostController) {
    composable(Route.LICENSE) {
        LicenseScreen(onNavigationClick = { navController.navigateUp() })
    }
}