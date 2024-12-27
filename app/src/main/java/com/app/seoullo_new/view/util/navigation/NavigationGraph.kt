package com.app.seoullo_new.view.util.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.app.seoullo_new.view.main.MainScreen
import com.app.seoullo_new.view.main.setting.LicenseScreen
import com.app.seoullo_new.view.map.DirectionScreen
import com.app.seoullo_new.view.placesDetail.PlaceDetailNearbyScreen
import com.app.seoullo_new.view.placesDetail.PlaceDetailScreen
import com.app.seoullo_new.view.placesList.PlacesListScreen
import com.app.seoullo_new.view.placesList.PlacesListViewModel
import com.app.seoullo_new.view.util.TravelJsonItemData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = Route.MAIN,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        },
    ) {
        mainScreenNavigation(navController)
        travelScreenNavigation(navController)
        settingScreenNavigation(navController)
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
    navigation(startDestination = Route.PLACE_LIST, route = Route.TRAVEL_ROUTE) {
        composable(
            Route.PLACE_LIST,
            arguments = listOf(navArgument("item") { type = NavType.StringType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Route.TRAVEL_ROUTE)
            }
            val viewModel: PlacesListViewModel = hiltViewModel(parentEntry)
            val itemJson = backStackEntry.arguments?.getString("item")
            val travelItem = itemJson?.let { Json.decodeFromString<TravelJsonItemData>(it) }

            PlacesListScreen(
                viewModel = viewModel,
                travelItem = travelItem ?: TravelJsonItemData(),
                onNavigationClick = { navController.navigateUp() },
                onNearbyItemClick = {
                    navController.navigate(
                        Route.PLACE_DETAIL_NEARBY
                            .replace(oldValue = "{place}", newValue = it)
                    ) {
                        // 화면 스크롤 및 상태 유지
                        popUpTo(Route.PLACE_LIST) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onItemClick = {
                    navController.navigate(
                        Route.PLACE_DETAIL
                            .replace(oldValue = "{place}", newValue = it)
                    ) {
                        // 화면 스크롤 및 상태 유지
                        popUpTo(Route.PLACE_LIST) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(
            Route.PLACE_DETAIL_NEARBY,
            arguments = listOf(navArgument("place") { type = NavType.StringType })
        ) {
            PlaceDetailNearbyScreen(
                onNavigationClick = { navController.navigateUp() },
                onDirectionClick = {
                    navController.navigate(
                        Route.DIRECTION
                            .replace(oldValue = "{latlng}", newValue = it)
                    )
                }
            )
        }
        composable(
            Route.PLACE_DETAIL,
            arguments = listOf(navArgument("place") { type = NavType.StringType })
        ) {
            PlaceDetailScreen(
                onNavigationClick = { navController.navigateUp() },
                onDirectionClick = {
                    navController.navigate(
                        Route.DIRECTION
                            .replace(oldValue = "{latlng}", newValue = it)
                    )
                }
            )
        }
        composable(
            Route.DIRECTION,
            arguments = listOf(navArgument("latlng") { type = NavType.StringType })
        ) {
            DirectionScreen(
                onNavigationClick = { navController.navigateUp() }
            )
        }
    }
}

fun NavGraphBuilder.settingScreenNavigation(navController: NavHostController) {
    // TODO: 추후 SettingViewModel 연결(로그아웃 등)
    composable(Route.LICENSE) {
        LicenseScreen(onNavigationClick = { navController.navigateUp() })
    }
}