package com.app.seoullo_new.view.util.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.app.seoullo_new.view.main.MainScreen
import com.app.seoullo_new.view.main.setting.LicenseScreen

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
    }
}

fun NavGraphBuilder.mainScreenNavigation(navController: NavHostController) {
    composable(Route.MAIN) {
        MainScreen(
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

fun NavGraphBuilder.settingScreenNavigation(navController: NavHostController) {
    composable(Route.LICENSE) {
        LicenseScreen(onNavigationClick = { navController.navigateUp() })
    }
}