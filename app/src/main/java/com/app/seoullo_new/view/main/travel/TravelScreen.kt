package com.app.seoullo_new.view.main.travel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Util.getAccommodations
import com.app.seoullo_new.utils.Util.getAttraction
import com.app.seoullo_new.utils.Util.getRestaurants
import com.app.seoullo_new.utils.Util.getShopping
import com.app.seoullo_new.utils.Util.loadDrawableResource
import com.app.seoullo_new.utils.Util.loadJsonFromAssets
import com.app.seoullo_new.utils.Util.loadTravelData
import com.app.seoullo_new.view.ui.theme.colorGridItem1
import com.app.seoullo_new.view.ui.theme.colorGridItem2
import com.app.seoullo_new.view.ui.theme.colorGridItem3
import com.app.seoullo_new.view.ui.theme.colorGridItem4
import com.app.seoullo_new.view.ui.theme.colorGridItem5
import com.app.seoullo_new.view.ui.theme.colorGridItem6
import com.app.seoullo_new.view.ui.theme.colorGridItem7
import com.app.seoullo_new.view.ui.theme.colorGridItem8
import com.app.seoullo_new.view.ui.theme.notosansFont
import com.app.seoullo_new.view.util.TravelJsonItemData
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun TravelScreen(
    travelOnClick: (TravelJsonItemData) -> Unit
) {
    Scaffold { innerPadding ->
        val context = LocalContext.current
        val jsonString = loadJsonFromAssets(context)
        val travelData = remember { loadTravelData(jsonString) }

        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp)
        ) {
            // 음식점 리스트 헤더
            item {
                Text(
                    text = stringResource(R.string.travel_title_restaurant),
                    fontWeight = FontWeight.Bold,
                    fontFamily = notosansFont,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(start = 8.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 음식점 리스트 항목
            item {
                TwoColumnGrid(
                    items = getRestaurants(travelData),
                    onItemClick = travelOnClick
                )
            }

            // 숙박시설 리스트 헤더
            item {
                Text(
                    text = stringResource(R.string.travel_title_accommodation),
                    fontWeight = FontWeight.Bold,
                    fontFamily = notosansFont,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 숙박시설 리스트 항목
            item {
                TwoColumnGrid(
                    items = getAccommodations(travelData),
                    onItemClick = travelOnClick
                )
            }

            // 관광명소 리스트 헤더
            item {
                Text(
                    text = stringResource(R.string.travel_title_attraction),
                    fontWeight = FontWeight.Bold,
                    fontFamily = notosansFont,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 관광명소 리스트 항목
            item {
                TwoColumnGrid(
                    items = getAttraction(travelData),
                    onItemClick = travelOnClick
                )
            }

            // 쇼핑 리스트 헤더
            item {
                Text(
                    text = stringResource(R.string.travel_title_shopping),
                    fontWeight = FontWeight.Bold,
                    fontFamily = notosansFont,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 쇼핑 리스트 항목
            item {
                TwoColumnGrid(
                    items = getShopping(travelData),
                    onItemClick = travelOnClick
                )
            }
        }
    }
}

@Composable
fun TwoColumnGrid(
    items: List<TravelJsonItemData>,
    onItemClick: (TravelJsonItemData) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { item ->
                    GridItem(
                        item = item,
                        onClick = { onItemClick(item) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // If the row has only one item, fill the remaining space
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun GridItem(
    item: TravelJsonItemData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colorOrBrush = generateColorOrBrush(item.color)
    val iconRes = remember { loadDrawableResource(context, item.icon) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .height(150.dp)
            .let { baseModifier ->
                when (colorOrBrush) {
                    is ColorOrBrush.SolidColor -> baseModifier.background(colorOrBrush.color)
                    is ColorOrBrush.GradientBrush -> baseModifier.background(colorOrBrush.brush)
                }
            }
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = item.name,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                fontFamily = notosansFont,
                color = Color.White
            )
            if (iconRes != 0) {
                GlideImage(
                    imageModel = iconRes,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.End),
                    placeHolder = painterResource(R.drawable.ic_seoul_symbol),
                    error = painterResource(R.drawable.ic_seoul_symbol)
                )
            }
        }
    }
}

// 리스트 색상 관련
sealed class ColorOrBrush {
    data class SolidColor(val color: Color) : ColorOrBrush()
    data class GradientBrush(val brush: Brush) : ColorOrBrush()
}

private fun generateColorOrBrush(id: Int): ColorOrBrush {
    return when (id) {
        0 -> ColorOrBrush.SolidColor(colorGridItem1)
        1 -> ColorOrBrush.SolidColor(colorGridItem2)
        2 -> ColorOrBrush.SolidColor(colorGridItem3)
        3 -> ColorOrBrush.SolidColor(colorGridItem4)
        4 -> ColorOrBrush.SolidColor(colorGridItem5)
        5 -> ColorOrBrush.SolidColor(colorGridItem6)
        6 -> ColorOrBrush.GradientBrush(
            Brush.linearGradient(
                colors = listOf(
                    colorGridItem7,
                    colorGridItem8
                )
            )
        )
        7 -> ColorOrBrush.SolidColor(colorGridItem8)
        else -> ColorOrBrush.SolidColor(colorGridItem1)
    }
}