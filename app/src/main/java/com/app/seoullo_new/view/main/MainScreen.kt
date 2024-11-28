package com.app.seoullo_new.view.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.main.home.HomeScreen
import com.app.seoullo_new.view.main.setting.SettingScreen
import com.app.seoullo_new.view.ui.theme.Color_92c8e0
import com.app.seoullo_new.view.ui.theme.Color_Gray500
import com.app.seoullo_new.view.util.Route
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

// 탭 3~4개
// 탭1: 메인 배너, popular location
// 탭2: travel info
// 탭3: 설정 창(다크 모드, 언어 설정, 로그아웃 등)

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { MainTopBar(viewModel) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // 시스템 바에 따른 패딩 적용
        ) {
            val tabs = listOf(
                Route.HOME,
                Route.TRAVEL,
                Route.SETTING
            )
            TabWithPager(tabs = tabs)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainTopBar(
    viewModel: MainViewModel
) {
    val profileImageUrl by viewModel.profileImageUrl.collectAsState()
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.seoullo),
                fontSize = 23.sp,
                style = TextStyle(
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color_92c8e0,
            titleContentColor = Color.White
        ),
        actions = {
            CircularProfileImage(profileImageUrl ?: "")
        }
    )
}

@Composable
fun CircularProfileImage(imageUrl: String, size: Dp = 40.dp) {
    Logging.d(imageUrl)
    GlideImage(
        imageModel = imageUrl, // URL 또는 기본값
        modifier = Modifier
            .size(size)
            .clip(CircleShape),
        placeHolder = painterResource(R.drawable.ic_user_default),
        error = painterResource(R.drawable.ic_user_default)
    )
}

// 탭과 페이지를 연결하고, 사용자가 탭을 눌렀을 때 페이지를 전환하거나 스와이프 시 탭이 변경되도록 한다.
@Composable
fun TabWithPager(
    tabs: List<String>
) {
    val pagerState = rememberPagerState { tabs.size }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        // ViewPager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f) // 남은 공간 차지
        ) { page ->
            when (tabs[page]) {
                Route.HOME -> HomeScreen()
                Route.TRAVEL -> TravelScreen()
                Route.SETTING -> SettingScreen()
            }
        }

        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

        // Tab Bar
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, item ->
                val isSelected = pagerState.currentPage == index
                val tabColor = if (isSelected) Color_92c8e0 else Color_Gray500
                Tab(
                    text = { Text(text = item, color = tabColor) },
                    icon = { Icon(imageVector = getIcon(item), contentDescription = null, tint = tabColor) },
                    selected = isSelected,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    }
                )
            }
        }
    }
}

fun getIcon(screen: String): ImageVector = when (screen) {
    Route.HOME -> Icons.Default.Home
    Route.TRAVEL -> Icons.Default.TravelExplore
    Route.SETTING -> Icons.Default.Settings
    else -> Icons.Default.Clear
}

@Composable
fun TravelScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("Screen 2")
    }
}

//@Composable
//fun SettingScreen() {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//        Text("Screen 3")
//    }
//}