package com.app.seoullo_new.view.main.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.domain.model.community.Post
import com.app.seoullo_new.utils.Util
import com.app.seoullo_new.view.util.CircularProfileImage
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun PostList(
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
//            .height(110.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProfileImage(
                    imageUrl = item.authorPhotoUrl,
                    size = 30.dp
                )

                Column(
                    modifier = modifier.padding(start = 10.dp)
                ) {
                    // 작성자
                    Text(
                        text = item.authorName,
                        fontSize = 12.sp,
                        style = TextStyle(
                            lineHeight = 12.sp,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)   // 내부 공백 제거
                        )
                    )

                    // 작성 시간
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
                                platformStyle = PlatformTextStyle(includeFontPadding = false),   // 내부 공백 제거
                                color = MaterialTheme.colorScheme.outline
                            )
                        )
                    }
                }
            }

            Spacer(modifier = modifier.height(9.dp))

            Text(
                text = item.title,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    lineHeight = 16.sp,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            )

            Spacer(modifier = modifier.height(7.dp))

            Text(
                text = item.content,
                fontSize = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    lineHeight = 14.sp,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            )

            Spacer(modifier = modifier.height(9.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.ThumbUp,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = modifier
                        .size(18.dp)
                        .padding(end = 4.dp),
                    contentDescription = null
                )
                Text(
                    text = item.likeCount.toString(),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = modifier.width(12.dp))

                Icon(
                    imageVector = Icons.Outlined.ModeComment,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = modifier
                        .size(18.dp)
                        .padding(end = 4.dp),
                    contentDescription = null
                )
                Text(
                    text = item.commentCount.toString(),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.outline
                    )
                )
            }

            if (item.imageUrls.isNotEmpty()) {
                Spacer(modifier = modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    GlideImage(
                        imageModel = item.imageUrls[0],
                        contentScale = ContentScale.Crop,
                        loading = { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) }
                    )
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
fun PostListPreview() {
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

    PostList(
        item = item
    ) { }
}