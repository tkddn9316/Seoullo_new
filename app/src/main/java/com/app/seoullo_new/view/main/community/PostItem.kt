package com.app.seoullo_new.view.main.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.domain.model.community.Post
import com.app.seoullo_new.utils.Util
import com.app.seoullo_new.view.ui.theme.notosansFont
import com.app.seoullo_new.view.util.CircularProfileImage

@Composable
fun PostItem(
    item: Post,
    modifier: Modifier = Modifier,
    communityOnClick: () -> Unit
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        onClick = { communityOnClick() },
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProfileImage(
                    imageUrl = item.authorPhotoUrl
                )

                Column(
                    modifier = modifier.padding(start = 10.dp)
                ) {
                    Text(
                        text = item.authorName,
                        fontFamily = notosansFont,
                        fontSize = 18.sp
                    )
                    Text(
                        text = item.title,
                        fontFamily = notosansFont,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
//                    Text(text = Util.getCurrentDateAndTime(item.createdAt))
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.WatchLater,
                    modifier = modifier
                        .size(18.dp)
                        .padding(end = 4.dp),
                    contentDescription = null
                )
                Text(text = Util.getCurrentDateAndTime(item.createdAt))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.ThumbUp,
                    modifier = modifier
                        .size(18.dp)
                        .padding(end = 4.dp),
                    contentDescription = null
                )
                Text(text = item.likeCount.toString())

                Spacer(modifier = modifier.width(9.dp))

                Icon(
                    imageVector = Icons.Outlined.ModeComment,
                    modifier = modifier
                        .size(18.dp)
                        .padding(end = 4.dp),
                    contentDescription = null
                )
                Text(text = item.commentCount.toString())
            }

            // TODO: 이미지 썸네일 추가 예정
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 360, heightDp = 110
)
@Composable
fun PostItemPreview() {
    val item = Post(
        id = "1W7uKF7IpDpM8Ew42pCI",
        title = "첫 번째 글입니다",
        content = "Firebase + Clean Architecture 게시판 샘플 🎉",
        authorId = "tN0dlFlwcrhBVoDtrELrszojJJD2",
        authorName = "토니",
        authorPhotoUrl = "https://lh3.googleusercontent.com/a/ACg8ocKu9yBA9n9LJ-TSD-Vi2oi1VaI-B749tcaEIQSOYZMlAZmcqQ=s96-c",
        createdAt = 1761090824862,
        likeCount = 13,
        commentCount = 40,
        imageUrls = emptyList()
    )

    PostItem(
        item = item
    ) { }
}