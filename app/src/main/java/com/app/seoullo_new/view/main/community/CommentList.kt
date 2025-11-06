package com.app.seoullo_new.view.main.community

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.data.utils.Util.DELETED_AUTHOR_ID
import com.app.domain.model.community.Comment
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Util
import com.app.seoullo_new.view.ui.theme.notosansFont
import com.app.seoullo_new.view.ui.theme.seoulloLightGray
import com.app.seoullo_new.view.util.CircularProfileImage
import kotlinx.coroutines.delay

@Composable
fun CommentList(
    item: Comment,
    isHighlighted: Boolean = false,
    triggerSeq: Long,
    modifier: Modifier = Modifier,
    onReplyCallback: (targetComment: Comment) -> Unit,
    onDeleteCommentClick: (commentId: String) -> Unit,
    onDeleteReplyClick: (commentId: String, replyId: String) -> Unit
) {
    val highlightColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.20f)
    val bgAnim = remember(item.id) { Animatable(Color.Transparent) }

    LaunchedEffect(isHighlighted, triggerSeq, item.id) {
        if (isHighlighted) {
            bgAnim.snapTo(Color.Transparent)
            bgAnim.animateTo(highlightColor, tween(160))
            delay(160)
            bgAnim.animateTo(Color.Transparent, tween(240))
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(bgAnim.value)
            ,
            verticalAlignment = Alignment.Top
        ) {
            val isDeleted = item.authorId == DELETED_AUTHOR_ID  // 삭제된 댓글 여부

            CircularProfileImage(
                imageUrl = item.authorPhotoUrl,
                size = 30.dp
            )

            Column(
                modifier = modifier
                    .padding(start = 10.dp)
                    .weight(1f)
            ) {
                // 작성자
                Text(
                    text = if (isDeleted) "-" else item.authorName,
                    fontSize = 12.sp,
                    style = TextStyle(
                        lineHeight = 12.sp,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    )
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.WatchLater,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = modifier
                            .size(16.dp)
                            .padding(end = 4.dp),
                        contentDescription = null
                    )
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
                    text = if (isDeleted) stringResource(R.string.has_been_deleted_comment) else item.text,
                    fontSize = 14.sp,
                    style = TextStyle(
                        fontFamily = notosansFont,
                        fontStyle = if (isDeleted) FontStyle.Italic else FontStyle.Normal
                    )
                )

                if (!isDeleted) {
                    Box(
                        modifier = modifier.clickable { onReplyCallback(item) }
                    ) {
                        Text(
                            text = stringResource(R.string.reply),
                            fontSize = 12.sp,
                            style = TextStyle(
                                color = seoulloLightGray,
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    }
                }
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

        if (item.replyList.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                item.replyList.forEach { reply ->
                    ReplyList(item = reply) { replyId ->
                        onDeleteReplyClick(item.id, replyId)
                    }
                }
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
        text = "ㅗ",
        replyList = listOf(
            Comment.Reply(
                authorId = "tN0dlFlwcrhBVoDtrELrszojJJD2",
                authorName = "토니",
                authorPhotoUrl = "",
                createdAt = 1761876525081,
                text = "ㅇㅇ"
            )
        )
    )

    CommentList(
        item = item,
        triggerSeq = 0,
        onReplyCallback = {},
        onDeleteCommentClick = {},
        onDeleteReplyClick = { _, _ -> }
    )
}