package com.app.seoullo_new.view.main.community.detail

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.app.seoullo_new.R

@Composable
fun DeleteNoticeDialog(
    text: String,
    onDone: () -> Unit,
    onClose: () -> Unit
) {
    AlertDialog(
        title = { Text(text = stringResource(R.string.post_delete)) },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
        },
        onDismissRequest = onClose,
        dismissButton = {
            TextButton(
                onClick = onClose,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDone,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(stringResource(R.string.confirm))
            }
        }
    )
}