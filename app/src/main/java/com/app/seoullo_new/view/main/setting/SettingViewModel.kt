package com.app.seoullo_new.view.main.setting

import com.app.domain.repository.SettingRepository
import com.app.domain.usecase.user.DeleteUserUseCase
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.view.base.BaseViewModel2
import com.app.seoullo_new.view.util.DialogState
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val settingRepository: SettingRepository,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val googleSignInClient: GoogleSignInClient
) : BaseViewModel2(dispatcherProvider) {

    private val _dialogState = MutableStateFlow(DialogState())
    val dialogState: StateFlow<DialogState> = _dialogState.asStateFlow()

    // 로그아웃 성공 시 네비게이션 이벤트 관리
    private val _navigateToSplash = MutableStateFlow(false)
    val navigateToSplash: StateFlow<Boolean> = _navigateToSplash

    fun openThemeDialog() = _dialogState.update { it.copy(isThemeDialogOpen = true) }
    fun openLanguageDialog() = _dialogState.update { it.copy(isLanguageDialogOpen = true) }
    fun openLogoutDialog() = _dialogState.update { it.copy(isLogoutDialogOpen = true) }

    fun closeThemeDialog() = _dialogState.update { it.copy(isThemeDialogOpen = false) }
    fun closeLanguageDialog() = _dialogState.update { it.copy(isLanguageDialogOpen = false) }
    fun closeLogoutDialog() = _dialogState.update { it.copy(isLogoutDialogOpen = false) }

    fun logout() {
        onIO {
            deleteUserUseCase()
            googleSignInClient.signOut().await()

            withContext(Dispatchers.Main) {
                closeLogoutDialog()
                _navigateToSplash.value = true
            }
        }
    }
}