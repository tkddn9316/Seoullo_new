package com.app.seoullo_new.view.main.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.seoullo_new.R
import com.app.seoullo_new.view.base.SeoulloAppBar
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@Composable
fun LicenseScreen(
    onNavigationClick: () -> Unit
) {
    Scaffold(
        topBar = {
            SeoulloAppBar(
                title = stringResource(R.string.license_title),
                onNavigationClick = onNavigationClick,
                showAction = false,
            ) { }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            LibrariesContainer(modifier = Modifier.fillMaxSize())
        }

    }
}