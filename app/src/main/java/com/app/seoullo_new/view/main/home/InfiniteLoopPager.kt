package com.app.seoullo_new.view.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.domain.model.Places
import com.app.seoullo_new.R
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 홈 화면 무한 롤링 배너
 */
const val AUTO_PAGE_CHANGE_DELAY = 3000L

@Composable
fun InfiniteLoopPager(
    modifier: Modifier = Modifier,
    item: List<Places>
) {
    val initialPage = rememberSaveable {
        var start = Int.MAX_VALUE / 2
        while (start % item.size != 0) {
            // 초기 페이지 0으로 이동
            start++
        }
        mutableIntStateOf(start)
    }
    val pagerState = rememberPagerState(
        pageCount = { Int.MAX_VALUE },
        initialPage = initialPage.intValue
    )
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = pagerState.currentPage) {
        while (true) {
            delay(AUTO_PAGE_CHANGE_DELAY)
            coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.height(200.dp),
        ) { index ->
            item.getOrNull(index % (item.size))?.let { place ->
                GlideImage(
                    imageModel = place.photoUrl,
                    contentScale = ContentScale.FillBounds,
                    loading = { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) },
                    failure = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_seoul_symbol),
                                contentDescription = null,
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                )
            }
        }

        PagerIndicator(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
                .padding(end = 16.dp, bottom = 16.dp),
            count = item.size,
            dotSize = 9.dp,
            spacedBy = 4.dp,
            currentPage = pagerState.currentPage % item.size,
            selectedColor = Color.White,
            unSelectedColor = Color.LightGray
        )
    }
}

@Composable
fun PagerIndicator(
    modifier: Modifier = Modifier,
    count: Int,
    dotSize: Dp,
    spacedBy: Dp,
    currentPage: Int,
    selectedColor: Color,
    unSelectedColor: Color
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacedBy),
            verticalAlignment = Alignment.CenterVertically
        ) {
            (0 until count).forEach { index ->
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .background(
                            color = if (index == currentPage) {
                                selectedColor
                            } else {
                                unSelectedColor
                            },
                            shape = CircleShape
                        )
                )
            }
        }
    }
}