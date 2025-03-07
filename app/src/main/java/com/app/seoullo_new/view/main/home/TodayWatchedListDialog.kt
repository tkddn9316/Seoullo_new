package com.app.seoullo_new.view.main.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.domain.model.TodayWatchedList
import com.app.seoullo_new.R

@Composable
fun TodayWatchedListDialog(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    list: List<TodayWatchedList>,
    watchedOnClick: (places: String, isNearby: String) -> Unit,
) {
    AlertDialog(
        title = { Text(text = stringResource(R.string.today_watched_list_title)) },
        text = {
            LazyColumn(
                modifier = modifier.fillMaxWidth(),
//                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = list
                ) { item ->
                    TodayWatchedListThumbnail(
                        item = item,
                        viewModel = viewModel,
                        watchedOnClick = watchedOnClick
                    )
                }
            }
        },
        onDismissRequest = viewModel::closeTodayWatchedListDialog,
        confirmButton = {
            TextButton(
                onClick = viewModel::closeTodayWatchedListDialog,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(stringResource(R.string.btn_close))
            }
        }
    )
}