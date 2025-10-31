package com.app.seoullo_new.view.main.community

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.domain.model.community.Comment
import com.app.seoullo_new.utils.Util
import com.app.seoullo_new.view.ui.theme.notosansFont
import com.app.seoullo_new.view.util.CircularProfileImage

@Composable
fun CommentList(
    item: Comment,
    modifier: Modifier = Modifier,
    onDeleteCommentClick: (commentId: String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        CircularProfileImage(
            imageUrl = item.authorPhotoUrl,
            size = 30.dp
        )

        Column(
            modifier = modifier.padding(start = 10.dp).weight(1f)
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
                onClick = { onDeleteCommentClick(item.id) }
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

@Preview(
    apiLevel = 34,
    showBackground = true,
    widthDp = 360, heightDp = 150
)
@Composable
fun CommentListPreview() {
    val item = Comment(
        authorId = "tN0dlFlwcrhBVoDtrELrszojJJD2",
        authorName = "토니",
        authorPhotoUrl = "https://lh3.googleusercontent.com/a/ACg8ocKu9yBA9n9LJ-TSD-Vi2oi1VaI-B749tcaEIQSOYZMlAZmcqQ=s96-c",
        createdAt = 1761876525081,
        text = "ㅗ"
    )

    CommentList(
        item = item,
        onDeleteCommentClick = {}
    )
}