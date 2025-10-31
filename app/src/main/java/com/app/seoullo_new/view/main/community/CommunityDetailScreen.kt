package com.app.seoullo_new.view.main.community

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.domain.model.common.ApiState
import com.app.domain.model.community.Comment
import com.app.domain.model.community.Post
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Util
import com.app.seoullo_new.view.base.ErrorScreen
import com.app.seoullo_new.view.base.LoadingOverlay
import com.app.seoullo_new.view.base.SeoulloAppBar
import com.app.seoullo_new.view.util.CircularProfileImage
import com.app.seoullo_new.view.util.PagerIndicator
import com.app.seoullo_new.view.util.advancedImePadding
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun CommunityDetailScreen(
    viewModel: CommunityDetailViewModel = hiltViewModel(),
    onNavigationClick: () -> Unit
) {
    val listState = rememberLazyListState()

    val postState by viewModel.post.collectAsStateWithLifecycle()
    val commentListState by viewModel.comments.collectAsStateWithLifecycle()
    val title by viewModel.title.collectAsStateWithLifecycle()
    val writerState by viewModel.isWriter.collectAsStateWithLifecycle()
    val hasLikedState by viewModel.hasLiked.collectAsStateWithLifecycle()
    val userInfo by viewModel.user.collectAsStateWithLifecycle()
    val commentTextState = rememberTextFieldState()

    val commentBoxHeightPx = remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    // dialog
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            SeoulloAppBar(
                title = title,
                onNavigationClick = onNavigationClick,
                showAction = false,
            ) { }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .advancedImePadding()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                when (val s = postState) {
                    is ApiState.Loading -> LoadingOverlay()
                    is ApiState.Error -> ErrorScreen(reason = s.message.orEmpty())
                    is ApiState.Success -> {
                        val post = s.data
                        post?.let {
                            viewModel.setTitle(it.title)
                            CommunityDetailView(
                                state = listState,
                                post = it,
                                commentList = commentListState,
                                isWriter = writerState,
                                hasLiked = hasLikedState,
                                bottomPadding = with(density) { commentBoxHeightPx.intValue.toDp() },
                                onPostModifyClick = { postId ->

                                },
                                onPostDeleteClick = { postId ->

                                },
                                onDeleteCommentClick = { commentId ->
                                    viewModel.openCommentDeleteDialog(commentId = commentId)
                                },
                                onLikeClick = { postId ->
                                    viewModel.setLike(postId = postId)
                                }
                            )
                        }
                    }

                    else -> Unit
                }
            }

            if (postState is ApiState.Success) {
                // 댓글 입력창은 화면 하단에 고정
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            commentBoxHeightPx.intValue = coordinates.size.height
                        }
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    CommentTextField(
                        userInfo = userInfo,
                        commentTextState = commentTextState,
                        onAddCommentClick = { comment ->
                            viewModel.addComment(comment = comment)
                        }
                    )
                }
            }
        }

        if (dialogState.isDeleteCommentDialogOpen) {
            DeleteCommentDialog(
                onDone = { viewModel.deleteSelectedComment() },
                onClose = { viewModel.closeCommentDeleteDialog() }
            )
        }
    }
}

@Composable
fun CommunityDetailView(
    state: LazyListState,
    post: Post,
    commentList: List<Comment>,
    isWriter: Boolean,
    hasLiked: Boolean,
    bottomPadding: Dp = 0.dp,
    modifier: Modifier = Modifier,
    onPostModifyClick: (postId: String) -> Unit,
    onPostDeleteClick: (postId: String) -> Unit,
    onDeleteCommentClick: (commentId: String) -> Unit,
    onLikeClick: (postId: String) -> Unit,
) {
    var previousSize by remember { mutableIntStateOf(commentList.size) }
    LaunchedEffect(commentList.size) {
        if (commentList.size > previousSize) {
            // 새로운 댓글이 추가되었을 때만
            state.scrollToItem(commentList.size)
        }
        previousSize = commentList.size
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = state,
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = bottomPadding + 16.dp
        )
    ) {
        item {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProfileImage(
                    imageUrl = post.authorPhotoUrl,
                    size = 30.dp
                )

                Column(
                    modifier = modifier.padding(start = 10.dp)
                ) {
                    // 작성자
                    Text(
                        text = post.authorName,
                        fontSize = 12.sp,
                        style = TextStyle(
                            lineHeight = 12.sp,
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
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
                            text = Util.getCurrentDateAndTime(post.createdAt),
                            fontSize = 12.sp,
                            style = TextStyle(
                                lineHeight = 12.sp,
                                platformStyle = PlatformTextStyle(includeFontPadding = false),
                                color = MaterialTheme.colorScheme.outline
                            )
                        )
                    }
                }

                // 옵션(삭제, 수정 등)
                if (isWriter) {
                    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

                    Spacer(modifier = modifier.weight(1f))

                    Box {
                        IconButton(
                            onClick = { isDropDownMenuExpanded = true }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MoreVert,
                                tint = MaterialTheme.colorScheme.outline,
                                contentDescription = null
                            )
                        }

                        DropdownMenu(
                            modifier = modifier.wrapContentSize(),
                            expanded = isDropDownMenuExpanded,
                            onDismissRequest = { isDropDownMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(R.string.post_modify))
                                },
                                onClick = {
                                    isDropDownMenuExpanded = false
                                    onPostModifyClick(post.id)
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(R.string.post_delete))
                                },
                                onClick = {
                                    isDropDownMenuExpanded = false
                                    onPostDeleteClick(post.id)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = modifier.height(9.dp))

            Text(
                text = post.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.height(7.dp))

            Text(
                text = post.content,
                fontSize = 18.sp
            )

            // 이미지 리스트
            if (post.imageUrls.isNotEmpty()) {
                val pagerState = rememberPagerState(pageCount = { post.imageUrls.size })

                Spacer(modifier = modifier.height(9.dp))
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.height(350.dp),
                    ) { index ->
                        post.imageUrls.getOrNull(index % (post.imageUrls.size))?.let { image ->
                            GlideImage(
                                imageModel = image,
                                contentScale = ContentScale.FillBounds,
                                loading = {
                                    Box(
                                        modifier = modifier.fillMaxSize()
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.align(
                                                Alignment.Center
                                            )
                                        )
                                    }

                                },
                                failure = {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_seoul_symbol),
                                            contentDescription = null,
                                            contentScale = ContentScale.Fit
                                        )
                                    }
                                }
                            )
                        }
                    }
                }

                if (post.imageUrls.size > 1) {
                    Spacer(modifier = modifier.height(6.dp))
                    PagerIndicator(
                        modifier = Modifier
                            .fillMaxWidth(),
                        count = post.imageUrls.size,
                        dotSize = 9.dp,
                        spacedBy = 4.dp,
                        currentPage = pagerState.currentPage % post.imageUrls.size,
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unSelectedColor = Color.LightGray,
                        dotAlignment = Alignment.Center
                    )
                }
            }

            // 좋아요 / 댓글 수
            Spacer(modifier = modifier.height(9.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        onLikeClick(post.id)
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (hasLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                            tint = if (hasLiked) Color.Red else MaterialTheme.colorScheme.outline,
                            modifier = modifier
                                .size(18.dp)
                                .padding(end = 4.dp),
                            contentDescription = null
                        )

                        Text(
                            text = post.likeCount.toString(),
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.outline
                            )
                        )
                    }
                }

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
                    text = commentList.size.toString(),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.outline
                    )
                )
            }

            if (commentList.isNotEmpty()) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 7.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }

        items(
            items = commentList,
            key = { it.id },
        ) { comment ->
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                CommentList(
                    item = comment
                ) { commentId ->
                    onDeleteCommentClick(commentId)
                }
            }
        }
    }
}