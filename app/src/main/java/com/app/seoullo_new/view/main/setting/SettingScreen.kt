package com.app.seoullo_new.view.main.setting

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

//    Scaffold(
//        modifier = modifier
//            .nestedScroll(scrollBehavior.nestedScrollConnection),
//        topBar = {
//            SettingTopBar(
//                scrollBehavior = scrollBehavior,
//                navigationOnClick = onNavigationClick
//            )
//        }
//    ) { innerPadding ->
//    }

}