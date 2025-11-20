package com.app.seoullo_new.view.main.community.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.seoullo_new.utils.Logging
import com.skydoves.landscapist.glide.GlideImage
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
fun ImageViewerDialog(
    imageUrl: String,
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    onImageDownloadClick: (imageUrl: String) -> Unit
) {
    val zoomState = rememberZoomState()
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.98f))
        ) {
            BackHandler(onBack = onClose)

            GlideImage(
                modifier = modifier
                    .fillMaxSize()
                    .zoomable(zoomState = zoomState),
                imageModel = imageUrl,
                loading = { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) },
                contentDescription = null,
                contentScale = ContentScale.Fit
            )

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = modifier.clip(CircleShape),
                    color = Color.Black.copy(alpha = 0.45f)
                ) {
                    IconButton(onClick = { onImageDownloadClick(imageUrl) }) {
                        Icon(
                            imageVector = Icons.Rounded.Download,
                            contentDescription = "다운로드",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = modifier.width(8.dp))

                Surface(
                    modifier = modifier.clip(CircleShape),
                    color = Color.Black.copy(alpha = 0.45f)
                ) {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "닫기",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}