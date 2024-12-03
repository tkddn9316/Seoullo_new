package com.app.seoullo_new.view.placeDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.seoullo_new.view.base.SeoulloAppBar

@Composable
fun PlaceDetailScreen(
    viewModel: PlaceDetailViewModel = hiltViewModel(),
    onNavigationClick: () -> Unit
) {
    Scaffold(
        topBar = {
            SeoulloAppBar(
                title = viewModel.getTitle(),
                onNavigationClick = onNavigationClick,
                showAction = true,
            ) { }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

        }
    }
}