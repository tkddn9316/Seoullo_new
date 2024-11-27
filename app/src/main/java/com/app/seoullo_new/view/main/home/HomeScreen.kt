package com.app.seoullo_new.view.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.app.domain.model.Weather
import com.app.seoullo_new.R
import com.app.seoullo_new.view.main.MainViewModel
import com.app.seoullo_new.view.ui.theme.Color_Cloudy
import com.app.seoullo_new.view.ui.theme.Color_Rainy
import com.app.seoullo_new.view.ui.theme.Color_Sunny
import com.app.seoullo_new.view.util.Route

// TODO: https://www.youtube.com/watch?v=GFhKfMY0L2E
// 기온, 강수확률, 풍속, 습도, 내일/모래 날씨, 미세먼지 등등...
@Composable
fun HomeScreen(viewModel: MainViewModel = hiltViewModel()) {
    val weather = viewModel.weatherListResult.collectAsState().value
    val skyWeather = weather.find { it.category == "SKY" }
    val ptyWeather = weather.find { it.category == "PTY" }
    val backgroundColor = when (skyWeather?.fcstValue) {
        "1" -> Color_Sunny   // 맑음
        "3" -> Color_Cloudy   // 구름많음
        "4" -> Color_Rainy   // 흐림
        else -> Color.White
    }
    val weatherIcon = when (ptyWeather?.fcstValue) {
        "0" -> {
            if (skyWeather?.fcstValue == "1") R.raw.weather_sun else R.raw.weather_cloudy
        }
        // TODO: 밤 시간 추가 필요
        "1" -> R.raw.weather_rainy_sun
        "2" -> R.raw.weather_snowy
        else -> 0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(14.dp)
    ) {
        Text(
            text = stringResource(id = R.string.seoul),
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        WeatherAnim(weatherIcon)

        Spacer(modifier = Modifier.height(6.dp))

        Temperature(weather)

//        Button(
//            onClick = {
//                navController.navigate(Route.Route.PlacesList.name)
//            },
//        ) {
//            Text(text = "호로로로로ㅗㅗ로로뢀")
//        }
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