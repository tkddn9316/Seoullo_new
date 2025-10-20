package com.app.seoullo_new.view.util

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.seoullo_new.R
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun CircularProfileImage(imageUrl: String, size: Dp = 40.dp) {
    GlideImage(
        imageModel = imageUrl, // URL 또는 기본값
        modifier = Modifier
            .size(size)
            .clip(CircleShape),
        placeHolder = painterResource(R.drawable.ic_user_default),
        error = painterResource(R.drawable.ic_user_default)
    )
}