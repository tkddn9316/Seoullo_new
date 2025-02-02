package com.app.seoullo_new.view.splash

import androidx.lifecycle.viewModelScope
import com.app.domain.model.User
import com.app.domain.model.Weather
import com.app.domain.model.common.ApiState
import com.app.domain.usecase.user.InsertUserUseCase
import com.app.domain.usecase.user.SelectUserUseCase
import com.app.domain.usecase.weather.WeatherUseCase
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.utils.LoginState
import com.app.seoullo_new.view.base.BaseViewModel2
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val selectUserUseCase: SelectUserUseCase,
    private val insertUserUseCase: InsertUserUseCase,
    private val weatherUseCase: WeatherUseCase,
    val googleSignInClient: GoogleSignInClient
) : BaseViewModel2(dispatcherProvider) {
    // 데이터를 캡슐화 하여 외부(뷰)에서 접근할 수 없도록 하고
    // 외부 접근 프로퍼티는 immutable 타입으로 제한해 변경할 수 없도록 한다.
    private val _isLogin = MutableStateFlow<LoginState>(LoginState.loading)
    val isLogin = _isLogin.asStateFlow()

    private val _apiLoadingMessage = MutableStateFlow("")
    val apiLoadingMessage = _apiLoadingMessage.asStateFlow()

    private val _weatherResult = MutableStateFlow(Weather())
    val weatherResult = _weatherResult.asStateFlow()

    init {
        getWeatherList()
    }

    private fun getWeatherList() {
        _apiLoadingMessage.value = "Getting Data."
        onIO {
            val weatherFlow = weatherUseCase(
                weatherApiKey = BuildConfig.OPEN_WEATHER_MAP_KEY,
                dustApiKey = BuildConfig.SEOUL_OPEN_API_KEY,
                sunriseApiKey = BuildConfig.TOUR_API_KEY
            ).flowOn(Dispatchers.IO)

            val loginFlow = selectUserUseCase().flowOn(Dispatchers.IO)

            combine(weatherFlow, loginFlow) { weatherResult, users ->
                when (weatherResult) {
                    is ApiState.Success -> {
                        // 날씨 데이터 저장
                        val weather = weatherResult.data ?: Weather()
                        _weatherResult.value = weather

                        _apiLoadingMessage.value = "Check Login."
                        delay(2000)
                        val isUserLoggedIn = users.any { it.auto == "Y" }
                        _isLogin.value = LoginState.IsUser(isUserLoggedIn)
                    }
                    is ApiState.Error -> {
                        _apiLoadingMessage.value = "Error..."
                    }
                    else -> {}
                }
            }
                .catch { Logging.e(it.message ?: "") }
                .collect()
        }
    }

    fun performGoogleSignIn(result: Task<GoogleSignInAccount>) {
        viewModelScope.launch {
            runCatching {
                result.getResult(ApiException::class.java)
            }.onSuccess { account ->
                onLoginSuccess(account)
            }.onFailure {
                Logging.e(it.message.toString())
            }
        }
    }

    private fun onLoginSuccess(account: GoogleSignInAccount) {
        onIO {
            val user = User(
                auto = "Y",    // 자동 로그인 설정
                name = account.displayName!!,
                email = account.email!!,
                photoUrl = account.photoUrl.toString()
            )
            insertUserUseCase(user)

            withContext(Dispatchers.Main) {
                _isLogin.value = LoginState.IsUser(true)
            }
        }
    }
}