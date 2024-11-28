package com.app.seoullo_new.view.util.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.domain.model.theme.DynamicTheme
import com.app.domain.model.theme.Language
import com.app.domain.model.theme.ThemeMode

val LocalDynamicTheme = compositionLocalOf { DynamicTheme.OFF }
val LocalThemeMode = compositionLocalOf { ThemeMode.SYSTEM }
val LocalLanguage = compositionLocalOf { Language.ENGLISH }
val LocalThemeViewModel = compositionLocalOf<ThemeViewModel> {
    error("CompositionLocal LocalThemeViewModel is not present")
}

@Composable
fun ThemeSettingProvider(
    themeViewModel: ThemeViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    themeViewModel.themeSetting.collectAsStateWithLifecycle().value.run {
        CompositionLocalProvider(
            LocalThemeViewModel provides themeViewModel,
            LocalDynamicTheme provides dynamicTheme,
            LocalThemeMode provides themeMode,
            LocalLanguage provides language,
            content = content
        )
    }
}