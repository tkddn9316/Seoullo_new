package com.app.seoullo_new.view.main

import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.seoullo_new.base.BaseComposeActivity
import com.app.seoullo_new.utils.Constants
import com.app.seoullo_new.utils.Route
import com.app.seoullo_new.view.ui.theme.Color_92c8e0
import com.app.seoullo_new.view.ui.theme.Color_Gray500
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// 탭 3~4개(navigation으로 구성, ViewPager X)
// 탭1: 메인 배너, popular location
// 탭2: travel info
// 탭3: 설정 창(로그아웃)

@AndroidEntryPoint
class MainActivity : BaseComposeActivity<MainViewModel>() {
    override val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Setup() {
        val navController = rememberNavController()

        val items = listOf(
            Constants.Screen.Home,
            Constants.Screen.Travel,
            Constants.Screen.Setting,
        )
        val pagerState = rememberPagerState { items.size }
        SetViewPager(pagerState, items, navController)

        InitNavHost(navController)
    }

    @Composable
    fun InitNavHost(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = Route.Route.Main.name
        ) {
            composable(route = Route.Route.Main.name) { Main(navController) }
            composable(route = Route.Route.PlacesList.name) { com.app.seoullo_new.view.placeList.Setup(navController) }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun SetViewPager(pagerState: PagerState, tabs: List<Constants.Screen>, navController: NavHostController) {
        val coroutineScope = rememberCoroutineScope()
        Column {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    when (page) {
//                        0 -> Main(viewModel.weatherListResult.collectAsState().value)
//                        1 -> com.app.seoullo_new.view.placeList.Setup()
                        0 -> Main(navController)
                        1 -> Screen2()
                        2 -> Screen3()
                    }
                }
            }
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Color_Gray500,
                thickness = 1.dp
            )
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.White,
                divider = {},   // 구분선 제거
                indicator = {}  // 인디케이터 제거
            ) {
                tabs.forEachIndexed { index, item ->
                    val isSelected = pagerState.currentPage == index
                    val tabColor = if (isSelected) Color_92c8e0 else Color_Gray500
                    Tab(
                        text = {
                            Text(
                                text = item.label,
                                color = tabColor
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = when (item) {
                                    Constants.Screen.Home -> Icons.Default.Home
                                    Constants.Screen.Travel -> Icons.Default.TravelExplore
                                    Constants.Screen.Setting -> Icons.Default.Settings
                                },
                                contentDescription = null,
                                tint = tabColor
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun Screen2() {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text("Screen 2")
        }
    }

    @Composable
    fun Screen3() {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text("Screen 3")
        }
    }
}