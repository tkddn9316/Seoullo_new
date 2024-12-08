package com.app.seoullo_new.view.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.seoullo_new.R
import com.app.seoullo_new.view.ui.theme.Color_ERROR
import com.app.seoullo_new.view.ui.theme.notosansFont
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ErrorScreen(
    reason: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (LocalInspectionMode.current) {
            Image(
                painter = painterResource(R.drawable.ic_no_result),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
        } else {
            GlideImage(
                imageModel = R.drawable.ic_no_result,
                modifier = Modifier.size(150.dp)
            )
        }

        Text(
            text = stringResource(R.string.screen_loading_failed),
            fontFamily = notosansFont,
            fontSize = 20.sp,
            color = Color_ERROR
        )
        Spacer(Modifier.height(7.dp))
        Text(
            text = stringResource(R.string.screen_loading_failed_reason, reason),
            fontFamily = notosansFont,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun ErrorScreenPreview() {
    CompositionLocalProvider(
        LocalContext provides LocalContext.current,
    ) {
        ErrorScreen("Sample reason for failure")
    }
}
