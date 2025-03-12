package com.app.seoullo_new.view.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
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
import com.app.domain.model.common.ApiState
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.ui.theme.Color_92c8e0
import com.app.seoullo_new.view.ui.theme.colorGridItem7
import com.app.seoullo_new.view.ui.theme.notosansFont
import com.app.seoullo_new.view.ui.theme.secondaryContainerLight
import com.app.seoullo_new.view.util.BackOnPressed
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun SplashRoute(
    viewModel: SplashViewModel = hiltViewModel(),
    onMoveMain: (weather: String, banner: String) -> Unit
) {
    val context = LocalContext.current
    val activity = remember { context as? Activity }

    // 가로 모드 비활성화 (세로 고정)
    LaunchedEffect(Unit) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
    BackOnPressed()

    /** 인앱 업데이트(강제) 관련
     * 추후 개발 진행 예정: https://velog.io/@mraz3068/Implementing-In-app-update-with-Compose
     * */
    // AppUpdateManager 초기화
//    val appUpdateManager: AppUpdateManager = remember { AppUpdateManagerFactory.create(context) }
//    val lifecycle = LocalLifecycleOwner.current.lifecycle
//    val lifecycleState by lifecycle.currentStateFlow.collectAsStateWithLifecycle()
//    val appUpdateResultLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartIntentSenderForResult()
//    ) { result ->
//        // 사용자가 업데이트를 취소하는 경우, 앱 종료
//        if (result.resultCode == Activity.RESULT_CANCELED) {
//            activity?.finish()
//        }
//    }
//    val nowVersionCode = BuildConfig.VERSION_CODE
//
//    LaunchedEffect(Unit) {
//        try {
//            viewModel.settingLoadingMessage("Update Check.")
//            val appUpdateInfo = appUpdateManager.appUpdateInfo.await()
//
//            // 업데이트가 가능한 상황인지 확인
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
//                val availableVersionCode = appUpdateInfo.availableVersionCode()
//                // 강제 업데이트가 필요한 상황인지 확인
//                if (availableVersionCode > nowVersionCode &&
//                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
//                ) {
//                    // 업데이트 시작
//                    appUpdateManager.startUpdateFlowForResult(
//                        appUpdateInfo,
//                        appUpdateResultLauncher,
//                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
//                    )
//                } else {
//                    // 강제 업데이트가 필요하지 않은 경우
//                    viewModel.getWeatherList()
//                }
//            } else {
//                // 강제 업데이트가 필요하지 않은 경우, 기존 로직 진행
//                viewModel.getWeatherList()
//            }
//        } catch (e: Exception) {
//            Logging.e(e.message ?: "")
//            viewModel.getWeatherList()
//        }
//    }
//
//    LaunchedEffect(key1 = lifecycleState) {
//        if (lifecycleState == Lifecycle.State.RESUMED) {
//            try {
//                val appUpdateInfo = appUpdateManager.appUpdateInfo.await()
//                // 앱 업데이트 도중 항상 업데이트 UI 가 보이도록 설정
//                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
//                    appUpdateManager.startUpdateFlowForResult(
//                        appUpdateInfo,
//                        appUpdateResultLauncher,
//                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
//                    )
//                }
//            } catch (e: Exception) {
//                Logging.e(e.message ?: "")
//            }
//        }
//    }
//    viewModel.getWeatherList()

    SplashScreen(
        viewModel = viewModel
    ) { weather, banner ->
        onMoveMain(weather, banner)
    }
}

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onMoveMain: (weather: String, banner: String) -> Unit
) {
    // 하단 애니메이션 세팅
    val retrySignal = rememberLottieRetrySignal()
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.splash_bus_anim),
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

    // API 관련
    val loadingMessage by viewModel.apiLoadingMessage.collectAsStateWithLifecycle()
    // 구글 로그인
    val loginState by viewModel.signInState.collectAsStateWithLifecycle()

    // 정보 받아온거
    val weatherData by viewModel.weatherResult.collectAsStateWithLifecycle()
    val bannerData by viewModel.bannerResult.collectAsStateWithLifecycle()

    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = modifier
                    .weight(1f)
                    .padding(start = 40.dp, end = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.seoullo),
                    color = Color_92c8e0,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )

                Spacer(modifier = modifier.height(30.dp))

                when (loginState) {
                    is ApiState.Success -> {
                        // 로그인 완료
                        val response = (loginState as ApiState.Success<GoogleIdTokenCredential?>).data
                        response?.let {
                            viewModel.insertUserCredential(it)
                        }

                        val weatherJson = Json.encodeToString(weatherData)
                        val encodedWeatherJson = Uri.encode(weatherJson)
                        val bannerJson = Json.encodeToString(bannerData)
                        val encodedBannerJson = Uri.encode(bannerJson)

                        onMoveMain(encodedWeatherJson, encodedBannerJson)
                    }
                    is ApiState.Error -> {
                        Logging.e((loginState as ApiState.Error).message ?: "")
                        GoogleSignInButton(
                            onClick = { viewModel.startGoogleSignIn() }
                        )
                    }
                    else -> {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp),
                            trackColor = secondaryContainerLight,
                            color = Color_92c8e0,
                            strokeCap = StrokeCap.Round,
                            gapSize = (-15).dp
                        )
                        Text(
                            text = loadingMessage,
                            fontFamily = notosansFont,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorGridItem7
                        )
                    }
                }
            }
            LottieAnimation(
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp),
                composition = composition,
                progress = { lottieAnimate.progress },
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(60.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White, // 배경 흰색
            contentColor = Color.Black // 글자색 검정
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_google),
                contentDescription = "Google Sign In",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.sign_in_with_google),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}