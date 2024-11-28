package com.app.seoullo_new.view.util.theme

import androidx.lifecycle.viewModelScope
import com.app.domain.model.theme.DynamicTheme
import com.app.domain.model.theme.Language
import com.app.domain.model.theme.ThemeMode
import com.app.domain.model.theme.ThemeSetting
import com.app.domain.repository.SettingRepository
import com.app.seoullo_new.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val settingRepository: SettingRepository
) : BaseViewModel() {

    private val _themeSetting = MutableStateFlow(ThemeSetting())
    val themeSetting = _themeSetting.asStateFlow()

    init {
        fetchThemes()
//        fetchLanguage()
    }

    private fun fetchThemes() {
        viewModelScope.launch {
            _themeSetting.update { settingRepository.fetchThemes() }
        }
    }

    fun updateDynamicTheme(theme: DynamicTheme) {
        _themeSetting.update { setting ->
            setting.copy(dynamicTheme = theme)
        }
        viewModelScope.launch {
            settingRepository.updateThemes(_themeSetting.value)
        }
    }

    fun updateThemeMode(theme: ThemeMode) {
        _themeSetting.update { setting ->
            setting.copy(themeMode = theme)
        }
        viewModelScope.launch {
            settingRepository.updateThemes(_themeSetting.value)
        }
    }

//    private fun fetchLanguage() {
//        viewModelScope.launch {
//            _themeSetting.update { settingRepository.fetchLanguage() }
//        }
//    }

    fun updateLanguage(language: Language) {
        _themeSetting.update { setting ->
            setting.copy(language = language)
        }
        viewModelScope.launch {
            settingRepository.updateLanguage(_themeSetting.value)
        }
    }
}