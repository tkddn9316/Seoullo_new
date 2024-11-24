package com.app.seoullo_new.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.seoullo_new.view.ui.theme.Color_92c8e0

@Composable
fun CircularProgress(viewModel: BaseViewModel = viewModel()) {
    // 맨 밑에 배치할 것
    if (viewModel.loading.value!!) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) { },   // 클릭 막기
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color_92c8e0)
        }
    }
}