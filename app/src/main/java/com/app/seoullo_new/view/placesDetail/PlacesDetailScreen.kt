package com.app.seoullo_new.view.placesDetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.base.SeoulloAppBar

@Composable
fun PlaceDetailScreen(
    viewModel: PlacesDetailViewModel = hiltViewModel(),
    onNavigationClick: () -> Unit
) {
    val placesState by viewModel.placesState.collectAsStateWithLifecycle()
//    val detailState by viewModel.placesDetailGoogleState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {

    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                SeoulloAppBar(
                    title = viewModel.getTitle(),
                    onNavigationClick = onNavigationClick,
                    showAction = false,
                ) { }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                Logging.e(placesState)
            }
        }

        // API 로딩 처리
    }
}