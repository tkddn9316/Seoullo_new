package com.app.seoullo_new.view.main.travel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.utils.Util.getRestaurants
import com.app.seoullo_new.utils.Util.loadJsonFromAssets
import com.app.seoullo_new.utils.Util.loadTravelData
import com.app.seoullo_new.view.util.TravelJsonItemData

@Composable
fun TravelScreen(
    travelOnClick: (TravelJsonItemData) -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            Modifier.padding(innerPadding)
        ) {
            val context = LocalContext.current
            val jsonString = loadJsonFromAssets(context)
            val travelData = remember { loadTravelData(jsonString) }

            // 식당 리스트
            Column(
                modifier = Modifier.padding(start = 24.dp, top = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.theme_title),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            TwoColumnGrid(
                items = getRestaurants(travelData),
                onItemClick = { item ->
                    Logging.d("Clicked on: $item")
                    travelOnClick(item)
                }
            )
        }
    }
}

@Composable
fun TwoColumnGrid(
    items: List<TravelJsonItemData>,
    onItemClick: (TravelJsonItemData) -> Unit // 클릭 리스너
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 열 수를 2로 고정
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp), // 열 간격
        verticalArrangement = Arrangement.spacedBy(8.dp)   // 행 간격
    ) {
        items(items.size) { index ->
            GridItem(
                item = items[index],
                onClick = { onItemClick(items[index]) } // 클릭 시 호출
            )
        }
    }
}

@Composable
fun GridItem(
    item: TravelJsonItemData,
    onClick: () -> Unit // 아이템 클릭 리스너
) {
    Box(
        modifier = Modifier
            .background(generateColor(item.color))
            .clickable(onClick = onClick) // 클릭 리스너 적용
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = item.name, style = MaterialTheme.typography.bodyLarge)
    }
}

private fun generateColor(id: Int): Color {
    return when (id) {
        0 -> Color.Red
        1 -> Color.Green
        2 -> Color.Blue
        3 -> Color.Yellow
        else -> Color.Cyan
    }
}