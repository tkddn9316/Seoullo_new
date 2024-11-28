package com.app.seoullo_new.view.main.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.domain.model.theme.DynamicTheme
import com.app.domain.model.theme.ThemeMode
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Constants.getDynamicThemeTitle
import com.app.seoullo_new.utils.Constants.getThemeModeTitle
import com.app.seoullo_new.view.util.RadioItem
import com.app.seoullo_new.view.util.theme.LocalDynamicTheme
import com.app.seoullo_new.view.util.theme.LocalThemeMode
import com.app.seoullo_new.view.util.theme.LocalThemeViewModel

@Preview
@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier.padding(start = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.setting_theme_setting),
                    fontWeight = FontWeight.Bold
                )
            }
            
            ThemeSetting { viewModel.openThemeDialog() }

            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

            if (dialogState.isThemeDialogOpen) {
                ThemeSettingDialog(viewModel)
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
fun ThemeSettingDialog(
    viewModel: SettingViewModel = hiltViewModel()
) {
    val themeViewModel = LocalThemeViewModel.current
    AlertDialog(
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.dynamic_theme),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
                DynamicTheme.entries.forEach { theme ->
                    RadioItem(
                        title = getDynamicThemeTitle(theme),
                        description = null,
                        value = theme.name,
                        selected = LocalDynamicTheme.current == theme
                    ) {
                        themeViewModel.updateDynamicTheme(theme)
                    }
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                )
                Text(
                    text = stringResource(R.string.dark_mode),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
                ThemeMode.entries.forEach { theme ->
                    RadioItem(
                        title = getThemeModeTitle(theme),
                        description = null,
                        value = theme.name,
                        selected = LocalThemeMode.current == theme
                    ) {
                        themeViewModel.updateThemeMode(theme)
                    }
                }
            }
        },
        onDismissRequest = viewModel::closeThemeDialog,
        confirmButton = {
            TextButton(
                onClick = viewModel::closeThemeDialog
            ) {
                Text(stringResource(R.string.confirm))
            }
        }
    )
}