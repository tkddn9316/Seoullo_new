package com.app.seoullo_new.view.main.community.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.domain.model.User
import com.app.domain.model.community.Comment
import com.app.seoullo_new.R
import com.app.seoullo_new.view.ui.theme.notosansFont
import com.app.seoullo_new.view.ui.theme.seoulloLightGray
import com.app.seoullo_new.view.util.CircularProfileImage
import kotlinx.coroutines.android.awaitFrame

@Composable
fun CommentTextField(
    userInfo: User,
    commentTextState: TextFieldState,
    isReply: Boolean,
    targetComment: Comment?,
    modifier: Modifier = Modifier,
    onAddCommentClick: (comment: String) -> Unit,
    onCloseReplyNoticeClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    // 답글 여부 알림
    if (isReply && targetComment != null) {
        Row(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = modifier.weight(1f),
                text = stringResource(R.string.reply_notice, targetComment.authorName)
            )

            IconButton(
                modifier = modifier.size(24.dp),
                onClick = { onCloseReplyNoticeClick() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    tint = MaterialTheme.colorScheme.outline,
                    contentDescription = null
                )
            }
        }
    }

    LaunchedEffect(
        key1 = isReply,
        key2 = targetComment?.id
    ) {
        // 리플 모드 ON일 때 키보드 올라오도록
        if (isReply) {
            // 구성 1프레임 후 포커스 -> 키보드 표시 -> 화면 안으로 스크롤
            awaitFrame()
            focusRequester.requestFocus()
            keyboard?.show()
            // 키보드가 가리면 올려주기 (예외 무시)
            runCatching { bringIntoViewRequester.bringIntoView() }
        }
    }

    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant
    )
    Row(
        modifier = modifier.padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularProfileImage(
            imageUrl = userInfo.photoUrl,
            size = 30.dp
        )
        Spacer(modifier = modifier.width(8.dp))
        // 댓글 입력창
        BasicTextField(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .bringIntoViewRequester(bringIntoViewRequester = bringIntoViewRequester)
                .focusRequester(focusRequester = focusRequester),
            state = commentTextState,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = notosansFont
            ),
            lineLimits = TextFieldLineLimits.MultiLine(
                maxHeightInLines = 3
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorator = { innerTextField ->
                Box(
                    modifier = modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(vertical = 6.dp, horizontal = 8.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // 텍스트 필드
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp) // 버튼과 간격
                        ) {
                            if (commentTextState.text.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.add_post_content_hint),
                                    fontSize = 16.sp,
                                    color = seoulloLightGray,
                                )
                            }
                            innerTextField()
                        }

                        // 업로드 버튼
                        if (commentTextState.text.isNotEmpty()) {
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = modifier
                                    .clip(shape = CircleShape)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(
                                            color = Color.Black
                                        )
                                    ) {
                                        focusManager.clearFocus()
                                        onAddCommentClick(commentTextState.text.toString())
                                        commentTextState.clearText()
                                    }
                                    .size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Upload,
                                    tint = Color.White,
                                    contentDescription = "Comment Upload"
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}