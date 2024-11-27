package com.app.seoullo_new.view.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.seoullo_new.view.main.MainScreen

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
    }
}

fun NavGraphBuilder.mainScreenNavigation(navController: NavHostController) {
    composable(Route.MAIN) {
        MainScreen(

        )
    }
}