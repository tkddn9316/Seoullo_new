package com.app.seoullo_new.view.main.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieRetrySignal
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Constants
import com.app.seoullo_new.utils.Logging
import kotlinx.coroutines.launch

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

@Composable
fun WeatherListAnim(
    modifier: Modifier = Modifier,
    weatherId: Int
) {
    val weatherStatus = Constants.WeatherStatus.fromId(weatherId)
    val iconRes = weatherStatus?.getIconRes() ?: 0

    if (iconRes > 0) {
        val retrySignal = rememberLottieRetrySignal()
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(iconRes),
            onRetry = { _: Int, previousException: Throwable ->
                Logging.e(previousException.message ?: "")
                retrySignal.awaitRetry()
                true
            }
        )
        val lottieAnimate = rememberLottieAnimatable()

        LaunchedEffect(composition) {
            lottieAnimate.animate(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                initialProgress = 0f
            )
        }

        LottieAnimation(
            modifier = modifier
                .size(30.dp)
                .padding(start = 8.dp),
            composition = composition,
            progress = { lottieAnimate.progress },
            contentScale = ContentScale.FillHeight
        )
    }
}