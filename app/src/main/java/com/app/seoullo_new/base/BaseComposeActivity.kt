package com.app.seoullo_new.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.ui.theme.Color_92c8e0
import com.app.seoullo_new.view.ui.theme.Seoullo_newTheme

abstract class BaseComposeActivity<VM : BaseViewModel> : ComponentActivity() {

    abstract val viewModel: VM

    protected val TAG: String by lazy {
        javaClass.simpleName
    }

    var loading by mutableStateOf(true)
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logging.e(TAG, "onCreate")

        viewModel.loading.observe(this) { isLoading ->
            loading = isLoading
        }

        setContent {
            Seoullo_newTheme {
                Surface(
                    color = Color.White, // 배경색을 원하는 색상으로 지정
                    modifier = Modifier.fillMaxSize() // 전체 화면을 채우도록 설정
                ) {
                    Setup()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // removeObserve
    }

    @Composable
    abstract fun Setup()

    @Composable
    fun CircularProgress() {
        // if(loading == true)일 경우
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color_92c8e0)
        }
    }
}