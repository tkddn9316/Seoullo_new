package com.app.seoullo_new.view.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.app.seoullo_new.view.ui.theme.Seoullo_newTheme
import com.app.seoullo_new.view.util.navigation.NavigationGraph
import com.app.seoullo_new.view.util.theme.LocalDynamicTheme
import com.app.seoullo_new.view.util.theme.LocalLanguage
import com.app.seoullo_new.view.util.theme.LocalThemeMode
import com.app.seoullo_new.view.util.theme.ThemeSettingProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            ThemeSettingProvider {
                Seoullo_newTheme(
                    dynamicTheme = LocalDynamicTheme.current,
                    themeMode = LocalThemeMode.current,
                    language = LocalLanguage.current
                ) {
                    NavigationGraph(navController)
                }
            }
        }
    }
}