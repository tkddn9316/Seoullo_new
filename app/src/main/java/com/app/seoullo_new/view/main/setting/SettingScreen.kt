package com.app.seoullo_new.view.main.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Constants.getLanguageTitle
import com.app.seoullo_new.view.util.navigation.Route
import com.app.seoullo_new.view.util.theme.LocalLanguage

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    settingOnClick: (String) -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val githubLink = stringResource(R.string.github_link)
    val bugReportLink = stringResource(R.string.bug_report_link)
    val playStoreLink = stringResource(R.string.play_store_link)

    val scrollState = rememberScrollState()
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

    // 로그아웃
    val navigateToSplash by viewModel.navigateToSplash.collectAsStateWithLifecycle()
    LaunchedEffect(
        key1 = navigateToSplash
    ) {
        if (navigateToSplash) {
            settingOnClick(Route.SPLASH)
        }
    }

    Scaffold { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            // Theme Setting
            Column(
                modifier = Modifier.padding(start = 24.dp, top = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.theme_title),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            ThemeSetting { viewModel.openThemeDialog() }
            LanguageSetting { viewModel.openLanguageDialog() }
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

            // About
            Column(
                modifier = Modifier.padding(start = 24.dp, top = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.about_title),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            SettingItem(
                modifier = Modifier.height(64.dp),
                title = stringResource(R.string.version_title),
                description = getAppVersionName(),
                onItemClick = { },
                showTrailingIcon = false,
                showLeadingIcon = true,
                leadingIcon = {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_setting_version),
                        contentDescription = null
                    )
                }
            )
            SettingItem(
                modifier = Modifier.height(64.dp),
                title = stringResource(R.string.license_title),
                description = stringResource(R.string.license_description),
                onItemClick = { settingOnClick(Route.LICENSE) },
                showTrailingIcon = true,
                showLeadingIcon = true,
                leadingIcon = {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_setting_license),
                        contentDescription = null
                    )
                }
            )
            SettingItem(
                modifier = Modifier.height(64.dp),
                title = stringResource(R.string.github_title),
                onItemClick = { uriHandler.openUri(githubLink) },
                showTrailingIcon = true,
                showLeadingIcon = true,
                leadingIcon = {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_setting_github),
                        contentDescription = null
                    )
                }
            )
            SettingItem(
                modifier = Modifier.height(64.dp),
                title = stringResource(R.string.bug_report_title),
                description = stringResource(R.string.bug_report_description),
                onItemClick = { uriHandler.openUri(bugReportLink) },
                showTrailingIcon = true,
                showLeadingIcon = true,
                leadingIcon = {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_setting_bug_report),
                        contentDescription = null
                    )
                }
            )
            SettingItem(
                modifier = Modifier.height(64.dp),
                title = stringResource(R.string.play_store_title),
                onItemClick = { uriHandler.openUri(playStoreLink) },
                showTrailingIcon = true,
                showLeadingIcon = true,
                leadingIcon = {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_setting_google_playstore),
                        contentDescription = null
                    )
                }
            )
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

            // Account
            Column(
                modifier = Modifier.padding(start = 24.dp, top = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.account_title),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            LogoutSetting { viewModel.openLogoutDialog() }


            // 팝업 상태 관리
            if (dialogState.isThemeDialogOpen) {
                ThemeSettingDialog(viewModel)
            }
            if (dialogState.isLanguageDialogOpen) {
                LanguageDialog(viewModel)
            }
            if (dialogState.isLogoutDialogOpen) {
                LogoutDialog(viewModel)
            }
        }
    }
}

@Composable
fun ThemeSetting(
    onItemClick: () -> Unit
) {
    SettingItem(
        title = stringResource(R.string.theme_settings_title),
        description = stringResource(R.string.theme_settings_description),
        onItemClick = onItemClick,
        showTrailingIcon = false,
        showLeadingIcon = true,
        leadingIcon = {
            Icon(
                ImageVector.vectorResource(id = R.drawable.ic_setting_dark_mode),
                contentDescription = stringResource(R.string.theme_settings_icon_description)
            )
        }
    )
}

@Composable
fun LanguageSetting(
    onItemClick: () -> Unit
) {
    SettingItem(
        title = stringResource(R.string.language_title),
        description = getLanguageTitle(LocalLanguage.current),
        onItemClick = onItemClick,
        showTrailingIcon = false,
        showLeadingIcon = true,
        leadingIcon = {
            Icon(
                ImageVector.vectorResource(id = R.drawable.ic_setting_language),
                contentDescription = stringResource(R.string.language_icon_description)
            )
        }
    )
}

@Composable
fun LogoutSetting(
    onItemClick: () -> Unit
) {
    SettingItem(
        modifier = Modifier.height(64.dp),
        title = stringResource(R.string.logout_title),
        onItemClick = onItemClick,
        showTrailingIcon = false,
        showLeadingIcon = true,
        leadingIcon = {
            Icon(
                ImageVector.vectorResource(id = R.drawable.ic_setting_logout),
                contentDescription = null
            )
        }
    )
}

@Composable
fun getAppVersionName(): String {
    val context = LocalContext.current
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: "Unknown"
    } catch (e: Exception) {
        "Unknown"
    }
}

// Preview
//@Preview
//@Composable
//fun TestSettingScreen() {
//    val mockViewModel = SettingViewModel(MockSettingRepository())
//    MaterialTheme {
//        SettingScreen(
//            viewModel = mockViewModel,
//            settingOnClick = {}
//        )
//    }
//}