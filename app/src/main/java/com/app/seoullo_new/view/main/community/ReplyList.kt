package com.app.seoullo_new.view.main.community

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SubdirectoryArrowRight
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.domain.model.community.Comment
import com.app.seoullo_new.utils.Util
import com.app.seoullo_new.view.ui.theme.notosansFont
import com.app.seoullo_new.view.ui.theme.seoulloLightGray
import com.app.seoullo_new.view.util.CircularProfileImage

@Composable
fun ReplyList(
    item: Comment.Reply,
    modifier: Modifier = Modifier,
    onDeleteReplyClick: (replyId: String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.SubdirectoryArrowRight,
            contentDescription = null,
            modifier = modifier.size(24.dp),
            tint = seoulloLightGray
        )

        Spacer(modifier = modifier.width(3.dp))

        CircularProfileImage(
            imageUrl = item.authorPhotoUrl,
            size = 30.dp
        )


        Column(
            modifier = modifier
                .padding(start = 10.dp)
                .weight(1f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 작성자
                Text(
                    text = item.authorName,
                    fontSize = 12.sp,
                    style = TextStyle(
                        lineHeight = 12.sp,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )

                Spacer(modifier = modifier.padding(start = 10.dp))

                Text(
                    text = Util.getCurrentDateAndTime(item.createdAt),
                    fontSize = 12.sp,
                    style = TextStyle(
                        lineHeight = 12.sp,
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        color = MaterialTheme.colorScheme.outline
                    )
                )
            }

            Text(
                text = item.text,
                fontSize = 14.sp,
                style = TextStyle(
                    fontFamily = notosansFont
                )
            )
        }

        if (item.isMine) {
            IconButton(
                modifier = modifier.size(30.dp),
                onClick = { onDeleteReplyClick(item.id) }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    tint = MaterialTheme.colorScheme.outline,
                    contentDescription = null
                )
            }
        }
    }
}
