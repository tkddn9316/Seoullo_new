package com.app.seoullo_new.view.main.setting

import com.app.domain.repository.SettingRepository
import com.app.seoullo_new.view.base.BaseViewModel
import com.app.seoullo_new.view.util.DialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingRepository: SettingRepository
) : BaseViewModel() {

    private val _dialogState = MutableStateFlow(DialogState())
    val dialogState: StateFlow<DialogState> = _dialogState.asStateFlow()

    fun openThemeDialog() = _dialogState.update { it.copy(isThemeDialogOpen = true) }

    fun closeThemeDialog() = _dialogState.update { it.copy(isThemeDialogOpen = false) }
}