package com.app.seoullo_new.view.main.community

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CommunityScreen(
    viewModel: CommunityViewModel = hiltViewModel(),
    communityOnClick: (String) -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0)   //  안드 15 불필요한 위/아래 패딩 제거
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            viewModel.setTestData()
        }
    }
}