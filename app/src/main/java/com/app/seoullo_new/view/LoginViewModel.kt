package com.app.seoullo_new.view

import androidx.lifecycle.viewModelScope
import com.app.domain.usecase.tourInfo.ManageUserUseCase
import com.app.seoullo_new.base.BaseViewModel
import com.app.seoullo_new.di.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val manageUserUseCase: ManageUserUseCase
) : BaseViewModel(dispatcherProvider) {
    // 데이터를 캡슐화 하여 외부(뷰)에서 접근할 수 없도록 하고
    // 외부 접근 프로퍼티는 immutable 타입으로 제한해 변경할 수 없도록 한다.
    private val _isLogin = MutableStateFlow(false)

    // flow를 LiveData로 변환해서 UI가 화면에 표시되는 동안에만 아이템을 관찰
    val isLogin = _isLogin.asStateFlow()

    init {
        isLoginCheck()
    }

    private fun isLoginCheck() {
        // DB 내부에서 로그인 유무 검색
        onIO {
            delay(1500)
//            manageUserUseCase.insertUser(User(1, "Y", "Y", "Y", "Y"))
            manageUserUseCase.selectAllUser()
                .flowOn(Dispatchers.IO)
                .filter { it.isNotEmpty() }
                .collect { user ->
                    // auto: Y일 시 자동 로그인
//                    user.any { it.auto == "Y" }
                    if (user.any { it.auto == "Y" }) {
                        _isLogin.value = true
                    }
                    // onDone(sealed class로 표기할 것->state)
                }
        }
    }
}