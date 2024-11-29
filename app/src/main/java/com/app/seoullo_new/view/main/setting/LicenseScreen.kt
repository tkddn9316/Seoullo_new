package com.app.seoullo_new.view.main.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.seoullo_new.R
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@Composable
fun LicenseScreen(
    onNavigationClick: () -> Unit
) {
    Scaffold(
        topBar = { LicenseTopAppBar(onNavigationClick) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            LibrariesContainer(modifier = Modifier.fillMaxSize())
        }

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun LicenseTopAppBar(
    onNavigationClick: () -> Unit
) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = stringResource(R.string.license_title),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp,
                )
            },
            navigationIcon = {
                IconButton(
                    modifier = Modifier.padding(4.dp),
                    onClick = onNavigationClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        )
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
    }
}