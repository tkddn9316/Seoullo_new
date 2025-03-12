package com.app.seoullo_new.view.main.setting

import androidx.lifecycle.viewModelScope
import com.app.domain.repository.SettingRepository
import com.app.domain.usecase.todayWatchedList.GetTodayWatchedListUseCase
import com.app.domain.usecase.user.DeleteUserUseCase
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.di.GoogleSignInManager
import com.app.seoullo_new.view.base.BaseViewModel2
import com.app.seoullo_new.view.util.DialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val settingRepository: SettingRepository,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val getTodayWatchedListUseCase: GetTodayWatchedListUseCase,
    private val googleSignInManager: GoogleSignInManager
) : BaseViewModel2(dispatcherProvider) {

    private val _dialogState = MutableStateFlow(DialogState())
    val dialogState: StateFlow<DialogState> = _dialogState.asStateFlow()

    // 로그아웃 성공 시 네비게이션 이벤트 관리
    private val _navigateToSplash = MutableStateFlow(false)
    val navigateToSplash: StateFlow<Boolean> = _navigateToSplash

    // 오늘 본 목록 숨기기 체크 상태 감지
    val switchState: StateFlow<Boolean> = settingRepository.getShowTodayWatchedList()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun openThemeDialog() = _dialogState.update { it.copy(isThemeDialogOpen = true) }
    fun openLanguageDialog() = _dialogState.update { it.copy(isLanguageDialogOpen = true) }
    fun openLogoutDialog() = _dialogState.update { it.copy(isLogoutDialogOpen = true) }

    fun closeThemeDialog() = _dialogState.update { it.copy(isThemeDialogOpen = false) }
    fun closeLanguageDialog() = _dialogState.update { it.copy(isLanguageDialogOpen = false) }
    fun closeLogoutDialog() = _dialogState.update { it.copy(isLogoutDialogOpen = false) }

    fun logout() {
        onIO {
            deleteUserUseCase()
            getTodayWatchedListUseCase.delete()
            googleSignInManager.signOut()
        }
        closeLogoutDialog()
        _navigateToSplash.value = true
    }

    fun updateShowTodayWatchedList(checked: Boolean) {
        onIO {
            settingRepository.updateShowTodayWatchedList(checked)
        }
    }
}