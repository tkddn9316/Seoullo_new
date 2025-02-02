package com.app.seoullo_new.view.main.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.domain.model.theme.Language
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Constants.getLanguageTitle
import com.app.seoullo_new.view.util.RadioItem
import com.app.seoullo_new.view.util.theme.LocalLanguage
import com.app.seoullo_new.view.util.theme.LocalThemeViewModel

@Composable
fun LanguageDialog(
    viewModel: SettingViewModel = hiltViewModel()
) {
    val themeViewModel = LocalThemeViewModel.current
    AlertDialog(
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.language_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
                Language.entries.forEach { language ->
                    RadioItem(
                        title = getLanguageTitle(language),
                        description = null,
                        value = language.name,
                        selected = LocalLanguage.current == language
                    ) {
                        themeViewModel.updateLanguage(language)
                    }
                }
            }
        },
        onDismissRequest = viewModel::closeLanguageDialog,
        confirmButton = {
            TextButton(
                onClick = viewModel::closeLanguageDialog,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(stringResource(R.string.btn_close))
            }
        }
    )
}