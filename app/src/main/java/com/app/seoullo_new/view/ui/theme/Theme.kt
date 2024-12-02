package com.app.seoullo_new.view.ui.theme

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.app.domain.model.theme.DynamicTheme
import com.app.domain.model.theme.Language
import com.app.domain.model.theme.ThemeMode
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.Locale

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    error = Color_ERROR
)

private val LightColorScheme = lightColorScheme(
    primary = Color_92c8e0,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    error = Color_ERROR

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

// https://material-foundation.github.io/material-theme-builder/
//private val lightScheme = lightColorScheme(
//    primary = Color_92c8e0,
//    onPrimary = onPrimaryLight,
//    primaryContainer = primaryContainerLight,
//    onPrimaryContainer = onPrimaryContainerLight,
//    secondary = secondaryLight,
//    onSecondary = onSecondaryLight,
//    secondaryContainer = secondaryContainerLight,
//    onSecondaryContainer = onSecondaryContainerLight,
//    tertiary = tertiaryLight,
//    onTertiary = onTertiaryLight,
//    tertiaryContainer = tertiaryContainerLight,
//    onTertiaryContainer = onTertiaryContainerLight,
//    error = errorLight,
//    onError = onErrorLight,
//    errorContainer = errorContainerLight,
//    onErrorContainer = onErrorContainerLight,
//    background = backgroundLight,
//    onBackground = onBackgroundLight,
//    surface = surfaceLight,
//    onSurface = onSurfaceLight,
//    surfaceVariant = surfaceVariantLight,
//    onSurfaceVariant = onSurfaceVariantLight,
//    outline = outlineLight,
//    outlineVariant = outlineVariantLight,
//    scrim = scrimLight,
//    inverseSurface = inverseSurfaceLight,
//    inverseOnSurface = inverseOnSurfaceLight,
//    inversePrimary = inversePrimaryLight,
//    surfaceDim = surfaceDimLight,
//    surfaceBright = surfaceBrightLight,
//    surfaceContainerLowest = surfaceContainerLowestLight,
//    surfaceContainerLow = surfaceContainerLowLight,
//    surfaceContainer = surfaceContainerLight,
//    surfaceContainerHigh = surfaceContainerHighLight,
//    surfaceContainerHighest = surfaceContainerHighestLight
//)

//private val darkScheme = darkColorScheme(
//    primary = Color_92c8e0,
//    onPrimary = onPrimaryDark,
//    primaryContainer = primaryContainerDark,
//    onPrimaryContainer = onPrimaryContainerDark,
//    secondary = secondaryDark,
//    onSecondary = onSecondaryDark,
//    secondaryContainer = secondaryContainerDark,
//    onSecondaryContainer = onSecondaryContainerDark,
//    tertiary = tertiaryDark,
//    onTertiary = onTertiaryDark,
//    tertiaryContainer = tertiaryContainerDark,
//    onTertiaryContainer = onTertiaryContainerDark,
//    error = errorDark,
//    onError = onErrorDark,
//    errorContainer = errorContainerDark,
//    onErrorContainer = onErrorContainerDark,
//    background = backgroundDark,
//    onBackground = onBackgroundDark,
//    surface = surfaceDark,
//    onSurface = onSurfaceDark,
//    surfaceVariant = surfaceVariantDark,
//    onSurfaceVariant = onSurfaceVariantDark,
//    outline = outlineDark,
//    outlineVariant = outlineVariantDark,
//    scrim = scrimDark,
//    inverseSurface = inverseSurfaceDark,
//    inverseOnSurface = inverseOnSurfaceDark,
//    inversePrimary = inversePrimaryDark,
//    surfaceDim = surfaceDimDark,
//    surfaceBright = surfaceBrightDark,
//    surfaceContainerLowest = surfaceContainerLowestDark,
//    surfaceContainerLow = surfaceContainerLowDark,
//    surfaceContainer = surfaceContainerDark,
//    surfaceContainerHigh = surfaceContainerHighDark,
//    surfaceContainerHighest = surfaceContainerHighestDark
//)

val lightScheme = lightColorScheme(
    primary = primaryLight, // Primary 색상 (톤 60)
    onPrimary = onPrimaryLight, // 흰색 텍스트 (톤 100)
    primaryContainer = primaryContainerLight, // 밝은 배경용 컨테이너 (톤 90)
    onPrimaryContainer = onPrimaryContainerLight, // 어두운 텍스트 (톤 10)
    secondary = secondaryLight, // Secondary 색상 (톤 50)
    onSecondary = onSecondaryLight, // 흰색 텍스트
    secondaryContainer = secondaryContainerLight, // 밝은 Secondary 컨테이너
    onSecondaryContainer = onSecondaryContainerLight, // 어두운 Secondary 텍스트
    background = backgroundLight, // 앱의 배경색 (톤 98)
    onBackground = onBackgroundLight, // 텍스트 색상 (톤 20)
    surface = surfaceLight, // 서피스 배경색 (톤 98)
    onSurface = onSurfaceLight, // 텍스트 색상 (톤 20)
    error = errorLight, // 에러 색상
    onError = onErrorLight, // 흰색 텍스트
    errorContainer = errorContainerLight, // 밝은 에러 컨테이너
    onErrorContainer = onErrorContainerLight // 어두운 에러 텍스트
)

val darkScheme = darkColorScheme(
    primary = primaryDark, // Primary 색상 (톤 80)
    onPrimary = onPrimaryDark, // 어두운 텍스트 (톤 20)
    primaryContainer = primaryContainerDark, // 어두운 Primary 컨테이너 (톤 30)
    onPrimaryContainer = onPrimaryContainerDark, // 밝은 텍스트 (톤 90)
    secondary = secondaryDark, // Secondary 색상 (톤 70)
    onSecondary = onSecondaryDark, // 어두운 Secondary 텍스트 (톤 30)
    secondaryContainer = secondaryContainerDark, // 어두운 Secondary 컨테이너
    onSecondaryContainer = onSecondaryContainerDark, // 밝은 텍스트
    background = backgroundDark, // 어두운 배경색 (톤 10)
    onBackground = onBackgroundDark, // 밝은 텍스트 (톤 90)
    surface = surfaceDark, // 어두운 서피스 색상 (톤 10)
    onSurface = onSurfaceDark, // 밝은 텍스트 (톤 90)
    error = errorDark, // 에러 색상
    onError = onErrorDark, // 어두운 텍스트
    errorContainer = errorContainerDark, // 어두운 에러 컨테이너
    onErrorContainer = onErrorContainerDark // 밝은 에러 텍스트
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

@Composable
fun Seoullo_newTheme(
    dynamicTheme: DynamicTheme = DynamicTheme.OFF,
    themeMode: ThemeMode = ThemeMode.LIGHT,
    language: Language = Language.ENGLISH,
    content: @Composable () -> Unit
) {
    val useDynamicColor = dynamicTheme == DynamicTheme.ON
    val useDarkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }
    val context = LocalContext.current

    SideEffect {
        val locale = when (language) {
            Language.ENGLISH -> Locale.ENGLISH
            Language.KOREA -> Locale.KOREAN
        }
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }

    val colorScheme = when {
        useDynamicColor -> {
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        useDarkTheme -> darkScheme.copy(background = backgroundDark) // reComposition 강제
        else -> lightScheme.copy(background = backgroundLight) // reComposition 강제
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !useDarkTheme
        }
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = Color_92c8e0
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}