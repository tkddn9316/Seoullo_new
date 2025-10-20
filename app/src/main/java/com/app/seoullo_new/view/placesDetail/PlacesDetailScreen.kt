package com.app.seoullo_new.view.placesDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.domain.model.PlacesDetail
import com.app.domain.model.common.ApiState
import com.app.domain.model.theme.Language
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.utils.Util.getLanguageCode
import com.app.seoullo_new.view.base.ErrorScreen
import com.app.seoullo_new.view.base.LoadingOverlay
import com.app.seoullo_new.view.base.SeoulloAppBar
import com.app.seoullo_new.view.util.theme.LocalLanguage

@Composable
fun PlaceDetailScreen(
    viewModel: PlacesDetailViewModel = hiltViewModel(),
    onNavigationClick: () -> Unit,
    onDirectionClick: (destination: String) -> Unit
) {
    val context = LocalContext.current
    val placesState by viewModel.placesState.collectAsStateWithLifecycle()
    val detailState by viewModel.placesDetailState.collectAsStateWithLifecycle()

    // 오늘 본 목록에서 왔는지, 리스트에서 왔는지
    val language = when (placesState.languageCode) {
        "en" -> Language.ENGLISH
        "ko" -> Language.KOREA
        else -> LocalLanguage.current   // 리스트에서 왔으면 앱에 등록한 언어로
    }

    LaunchedEffect(Unit) {
        viewModel.getPlacesDetail(
            languageCode = language
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(topBar = {
            SeoulloAppBar(
                title = viewModel.getTitle(),
                onNavigationClick = onNavigationClick,
                showAction = false,
            ) { }
        }) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                Logging.e(placesState)
                when (detailState) {
                    is ApiState.Initial -> {}
                    is ApiState.Loading -> {
                        // API 로딩 처리
                        LoadingOverlay()
                    }

                    is ApiState.Success -> {
                        // DB 넣기(리스트에서 진입했을 경우만)
                        if (placesState.languageCode.isEmpty()) {
                            viewModel.insertTodayWatchedList(
                                data = placesState,
                                isNearby = false,
                                languageCode = getLanguageCode(
                                    context = context, language = language
                                )
                            )
                        }

                        val placesDetail =
                            (detailState as ApiState.Success<PlacesDetail>).data ?: PlacesDetail()
                        PlacesDetailView(
                            viewModel = viewModel,
                            placesDetail = placesDetail,
                            places = placesState,
                            onDirectionClick = onDirectionClick
                        )
                    }

                    is ApiState.Error -> {
                        val error = (detailState as ApiState.Error).message
                        ErrorScreen(error ?: "")
                    }
                }
            }
        }
    }
}