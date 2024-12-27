package com.app.seoullo_new.view.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.app.domain.model.Direction
import com.app.domain.model.Weather
import com.app.domain.model.common.ApiState
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.base.LoadingOverlay
import com.google.gson.Gson

// TODO: https://www.youtube.com/watch?v=GFhKfMY0L2E
// 기온, 강수확률, 풍속, 습도, 내일/모래 날씨, 미세먼지 등등...
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
//    val weather = viewModel.weatherListResult.collectAsState().value
//    val skyWeather = weather.find { it.category == "SKY" }
//    val ptyWeather = weather.find { it.category == "PTY" }
//    val backgroundColor = when (skyWeather?.fcstValue) {
//        "1" -> Color_Sunny   // 맑음
//        "3" -> Color_Cloudy   // 구름많음
//        "4" -> Color_Rainy   // 흐림
//        else -> Color.White
//    }
//    val weatherIcon = when (ptyWeather?.fcstValue) {
//        "0" -> {
//            if (skyWeather?.fcstValue == "1") R.raw.weather_sun else R.raw.weather_cloudy
//        }
//        // TODO: 밤 시간 추가 필요
//        "1" -> R.raw.weather_rainy_sun
//        "2" -> R.raw.weather_snowy
//        else -> 0
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(backgroundColor)
//            .padding(14.dp)
//    ) {
//        Text(
//            text = stringResource(id = R.string.seoul),
//            color = Color.White,
//            fontSize = 26.sp,
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(14.dp))
//
//        WeatherAnim(weatherIcon)
//
//        Spacer(modifier = Modifier.height(6.dp))
//
//        Temperature(weather)
//    }
    // TODO: TEST
    val scrollState = rememberScrollState()
    val state by viewModel.test.collectAsStateWithLifecycle()

    Column(
        Modifier.verticalScroll(scrollState)
    ) {
        Button(
            onClick = {
                viewModel.test()
            }
        ) {
            Text(
                text = "테스트"
            )
        }

        when (state) {
            is ApiState.Initial -> {}
            is ApiState.Loading -> {
                // API 로딩 처리
                LoadingOverlay()
            }
            is ApiState.Success -> {
                val placesDetail = (state as ApiState.Success<Direction>).data ?: Direction("", emptyList())
                val result = Gson().toJson(placesDetail)
                Logging.e(result)
                Text(
                    text = result
                )
            }
            is ApiState.Error -> {
                val error = (state as ApiState.Error).message
                Logging.e(error!!)
            }
        }
    }

}

@Composable
fun WeatherAnim(weatherIcon: Int) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(weatherIcon))
    val lottieAnimatable = rememberLottieAnimatable()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LaunchedEffect(composition) {
            lottieAnimatable.animate(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                initialProgress = 0f
            )
        }

        LottieAnimation(
            composition = composition,
            progress = lottieAnimatable.progress,
            contentScale = ContentScale.FillHeight
        )
    }
}

@Composable
fun Temperature(weather: List<Weather>) {
    val tmpWeather = weather.find { it.category == "TMP" }
    val tmp = tmpWeather?.fcstValue ?: "0"
    Row {
        Text(
            text = tmp + stringResource(id = R.string.temp_symbol),
            color = Color.White,
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold
        )
    }
}