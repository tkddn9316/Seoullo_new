package com.app.seoullo_new.view.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.app.seoullo_new.view.main.home.HomeScreen
import com.app.seoullo_new.view.ui.theme.Color_92c8e0
import com.app.seoullo_new.view.ui.theme.Color_Gray500
import com.app.seoullo_new.view.util.Route
import kotlinx.coroutines.launch

// 탭 3~4개
// 탭1: 메인 배너, popular location
// 탭2: travel info
// 탭3: 설정 창(로그아웃)

@Composable
fun MainScreen() {
    Scaffold { paddingValues ->
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

@Composable
fun SettingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("Screen 3")
    }
}
