package com.app.seoullo_new.view

import com.app.domain.model.User
import com.app.domain.usecase.user.DeleteUserUseCase
import com.app.domain.usecase.user.InsertUserUseCase
import com.app.domain.usecase.user.SelectUserUseCase
import com.app.seoullo_new.base.BaseViewModel2
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.utils.LoginState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val selectUserUseCase: SelectUserUseCase,
    private val insertUserUseCase: InsertUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : BaseViewModel2(dispatcherProvider) {
    // 데이터를 캡슐화 하여 외부(뷰)에서 접근할 수 없도록 하고
    // 외부 접근 프로퍼티는 immutable 타입으로 제한해 변경할 수 없도록 한다.
    private val _isLogin = MutableStateFlow<LoginState>(LoginState.loading)
    val isLogin = _isLogin.asStateFlow()

    fun isLoginCheck() {
        // DB 내부에서 로그인 유무 검색
        onIO {
            delay(2000)
            selectUserUseCase()
                .flowOn(Dispatchers.IO)
                .collect { user ->
                    // auto: Y일 시 자동 로그인
                    Logging.e(user)
                    _isLogin.value = LoginState.IsUser(user.any { it.auto == "Y" })
                }
        }
    }

    fun onLoginSuccess(firebaseUser: FirebaseUser) {
        onIO {
            val user = User(
                0,
                "Y",    // 자동 로그인 설정
                firebaseUser.displayName!!,
                firebaseUser.email!!,
                firebaseUser.photoUrl.toString()
            )
            insertUserUseCase(user)

            withContext(Dispatchers.Main) {
                _isLogin.value = LoginState.IsUser(true)
            }
        }
    }
}