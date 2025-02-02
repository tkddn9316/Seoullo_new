package com.app.seoullo_new.view.main.setting

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.seoullo_new.R

@Composable
fun LogoutDialog(
    viewModel: SettingViewModel = hiltViewModel()
) {
    AlertDialog(
        text = {
            Text(
                text = stringResource(R.string.logout_dialog_contents),
                style = MaterialTheme.typography.titleMedium
            )
        },
        onDismissRequest = viewModel::closeLogoutDialog,
        confirmButton = {
            TextButton(
                onClick = viewModel::logout,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = viewModel::closeLogoutDialog,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}