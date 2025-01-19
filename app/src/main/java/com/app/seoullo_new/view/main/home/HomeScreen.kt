package com.app.seoullo_new.view.main.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieRetrySignal
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.ui.theme.notosansFont
import kotlinx.coroutines.launch

// TODO: https://www.youtube.com/watch?v=GFhKfMY0L2E
// 기온, 강수확률, 풍속, 습도, 내일/모래 날씨, 미세먼지 등등...
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // 날씨 결과
    val weatherList by viewModel.weatherResult.collectAsStateWithLifecycle()
    val backgroundColor by viewModel.homeBackgroundColor.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessages.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Brush.linearGradient(colors = backgroundColor))
                .padding(innerPadding),
            contentPadding = PaddingValues(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 온도
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WeatherAnim(
                        viewModel = viewModel
                    )
                    Spacer(modifier = Modifier.width(8.dp))
//                    val temperature = weatherList.find { it.category == WeatherCategory.Temperature.category }?.fcstValue ?: "0"
                    val temperature = "0"
                    Text(
                        text = stringResource(id = R.string.temp_symbol, temperature),
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 기타 정보
            item {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(0.dp, 14.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
//                    val temperature = weatherList.find { it.category == WeatherCategory.Temperature.category }?.fcstValue ?: "0"
                    val temperature = "0"
                    Row (
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = modifier.weight(0.5f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "1",
                                fontFamily = notosansFont,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                        }
                        Column(
                            modifier = modifier.weight(0.5f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "1",
                                fontFamily = notosansFont,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        if (!errorMessage.isNullOrEmpty()) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun WeatherAnim(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val weatherIcon by viewModel.weatherIcon.collectAsStateWithLifecycle()

    if (weatherIcon > 0) {
        val retrySignal = rememberLottieRetrySignal()
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(weatherIcon),
            onRetry = { _: Int, previousException: Throwable ->
                Logging.e(previousException.message ?: "")
                retrySignal.awaitRetry()
                true
            }
        )
        val lottieAnimate = rememberLottieAnimatable()
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(composition) {
            lottieAnimate.animate(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                initialProgress = 0f
            )
        }

        Box {
            LottieAnimation(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp),
                composition = composition,
                progress = { lottieAnimate.progress },
                contentScale = ContentScale.FillHeight
            )

            // Retry
            if (retrySignal.isAwaitingRetry) {
                Button(
                    onClick = { coroutineScope.launch { retrySignal.retry() } }
                ) {
                    Text(
                        text = stringResource(R.string.retry)
                    )
                }
            }
        }
    }
}

// 미세먼지 데이터는 에어코리아 API를 불러와 처리
// String airConditon = 'http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?'
//        'stationName=$obs&dataTerm=DAILY&pageNo=1&ver=1.0'
//        '&numOfRows=1&returnType=json&serviceKey=$apiKey';