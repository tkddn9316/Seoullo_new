package com.app.seoullo_new.view.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.seoullo_new.R
import com.app.seoullo_new.view.base.SeoulloAppBar

@Composable
fun DirectionScreen(
    viewModel: MapViewModel = hiltViewModel(),
    onNavigationClick: () -> Unit
) {
    Scaffold(
        topBar = {
            SeoulloAppBar(
                title = stringResource(R.string.direction_title),
                onNavigationClick = onNavigationClick,
                showAction = false,
            ) { }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            MapScreen(
                viewModel = viewModel
            )
        }
    }
}