package com.app.seoullo_new.view

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.domain.model.User
import com.app.domain.usecase.tourInfo.GetTourInfoUseCase
import com.app.domain.usecase.tourInfo.ManageUserUseCase
import com.app.seoullo_new.base.BaseViewModel
import com.app.seoullo_new.utils.Logging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val manageUserUseCase: ManageUserUseCase
) : BaseViewModel() {
    private val isLogin = MutableStateFlow(false)
    val _isLogin = isLogin.asLiveData()

    init {
        isLoginCheck()
    }

    private fun isLoginCheck() {
        // DB 내부에서 로그인 유무 검색
        viewModelScope.launch {
            delay(1500)
//            manageUserUseCase.insertUser(User(1, "Y", "Y", "Y", "Y"))
            manageUserUseCase.selectAllUser()
                .flowOn(Dispatchers.IO)
                .filter { it.isNotEmpty() }
                .collect { user ->
                    // auto: Y일 시 자동 로그인
                    user.any { it.auto == "Y" }
//                    if (user.any {it.auto == "Y"}) {
//                        isLogin.value = true
//                    }
                    // onDone
                }
        }
    }
}