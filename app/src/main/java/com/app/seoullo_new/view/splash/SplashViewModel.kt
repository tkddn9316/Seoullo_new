package com.app.seoullo_new.view.splash

import com.app.domain.model.Places
import com.app.domain.model.User
import com.app.domain.model.Weather
import com.app.domain.model.common.ApiState
import com.app.domain.usecase.places.GetPlacesListUseCase
import com.app.domain.usecase.user.InsertUserUseCase
import com.app.domain.usecase.user.SelectUserUseCase
import com.app.domain.usecase.weather.WeatherUseCase
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.di.GoogleSignInManager
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.base.BaseViewModel2
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val selectUserUseCase: SelectUserUseCase,
    private val insertUserUseCase: InsertUserUseCase,
    private val weatherUseCase: WeatherUseCase,
    private val placesListUseCase: GetPlacesListUseCase,
    private val googleSignInManager: GoogleSignInManager
) : BaseViewModel2(dispatcherProvider) {
    // 데이터를 캡슐화 하여 외부(뷰)에서 접근할 수 없도록 하고
    // 외부 접근 프로퍼티는 immutable 타입으로 제한해 변경할 수 없도록 한다.
    private val _signInState = MutableStateFlow<ApiState<GoogleIdTokenCredential?>>(ApiState.Initial())
    val signInState = _signInState.asStateFlow()

    private val _apiLoadingMessage = MutableStateFlow("")
    val apiLoadingMessage = _apiLoadingMessage.asStateFlow()

    private val _weatherResult = MutableStateFlow(Weather())
    val weatherResult = _weatherResult.asStateFlow()

    private val _bannerResult = MutableStateFlow<List<Places>>(emptyList())
    val bannerResult = _bannerResult.asStateFlow()

    init {
        getWeatherList()
    }

    private fun getWeatherList() {
        settingLoadingMessage("Getting Data.")
        onIO {
            val weatherFlow = weatherUseCase(
                weatherApiKey = BuildConfig.OPEN_WEATHER_MAP_KEY,
                dustApiKey = BuildConfig.SEOUL_OPEN_API_KEY,
                sunriseApiKey = BuildConfig.TOUR_API_KEY
            ).flowOn(Dispatchers.IO)

            val tokenId = selectUserUseCase()
                .flowOn(Dispatchers.IO)
                .map { it.firstOrNull()?.tokenId ?: "" }
                .first()

            val bannerFlow = placesListUseCase.getBannerData(
                serviceUrl = "KorService2",
                serviceKey = BuildConfig.TOUR_API_KEY,
                contentTypeId = "15",
                category = "A0208"
            ).flowOn(Dispatchers.IO)

            combine(weatherFlow, bannerFlow) { weatherResult, bannerResult ->
                when {
                    // 둘 다 성공해야 함
                    weatherResult is ApiState.Success && bannerResult is ApiState.Success -> {
                        // 날씨 데이터 저장
                        val weather = weatherResult.data ?: Weather()
                        _weatherResult.value = weather

                        // 배너 데이터 저장
                        val banner = bannerResult.data ?: emptyList()
                        _bannerResult.value = banner

                        // 로그인 체크
                        settingLoadingMessage("Check Login.")
                        delay(2000)

                        googleSignInManager.signIn(
                            token = tokenId,
                            isAuto = true
                        )
                            .flowOn(Dispatchers.IO)
                            .collect {
                                _signInState.value = it
                            }
                    }
                    weatherResult is ApiState.Error || bannerResult is ApiState.Error -> {
                        settingLoadingMessage("Error...")
                    }
                    else -> {}
                }
            }
                .catch { Logging.e(it.message ?: "") }
                .collect()
        }
    }

    private fun settingLoadingMessage(value: String) {
        _apiLoadingMessage.value = value
    }

    fun startGoogleSignIn() {
        onIO {
            googleSignInManager.signIn(
                token = "",
                isAuto = false
            )
                .collectLatest {
                    _signInState.value = it
                }
        }
    }

    fun insertUserCredential(account: GoogleIdTokenCredential) {
        onIO {
            val user = User(
                name = account.displayName ?: "",
                email = account.id,
                tokenId = account.idToken,
                photoUrl = account.profilePictureUri.toString()
            )
            insertUserUseCase(user)
        }
    }
}