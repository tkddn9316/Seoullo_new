package com.app.seoullo_new.view.main.community.detail

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.domain.model.User
import com.app.domain.model.common.ApiState
import com.app.domain.model.community.Comment
import com.app.domain.model.community.Post
import com.app.domain.model.theme.ImageViewerState
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Util
import com.app.seoullo_new.view.base.ErrorScreen
import com.app.seoullo_new.view.base.LoadingOverlay
import com.app.seoullo_new.view.base.SeoulloAppBar
import com.app.seoullo_new.view.util.CircularProfileImage
import com.app.seoullo_new.view.util.DialogState
import com.app.seoullo_new.view.util.Highlight
import com.app.seoullo_new.view.util.PagerIndicator
import com.app.seoullo_new.view.util.advancedImePadding
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.flow.first

/**
 * SRP & 결합도 개선 리팩토링 버전
 * - 외형/로직/사이드이펙트를 구분하여 서브 컴포넌트로 분리
 * - 내부 구성요소: Header, Images, ReactionBar, CommentsList, BottomCommentInput, DialogHosts, OverlayHandlers
 */
@Composable
fun CommunityDetailScreen(
    viewModel: CommunityDetailViewModel = hiltViewModel(),
    onNavigationClick: () -> Unit,
    onPostModifyClick: (postId: String) -> Unit
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()

    val postState by viewModel.post.collectAsStateWithLifecycle()
    val deletePostState by viewModel.deletePostState.collectAsStateWithLifecycle()
    val commentListState by viewModel.comments.collectAsStateWithLifecycle()
    val title by viewModel.title.collectAsStateWithLifecycle()
    val writerState by viewModel.isWriter.collectAsStateWithLifecycle()
    val hasLikedState by viewModel.hasLiked.collectAsStateWithLifecycle()
    val userInfo by viewModel.user.collectAsStateWithLifecycle()
    val likeState by viewModel.likeState.collectAsStateWithLifecycle()

    val commentTextState = rememberTextFieldState()

    val commentBoxHeightPx = remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()
    val dialogState2 by viewModel.dialogState2.collectAsStateWithLifecycle()
    val dialogState3 by viewModel.dialogState3.collectAsStateWithLifecycle()
    val dialogImageViewState by viewModel.dialogState4.collectAsStateWithLifecycle()

    // Reply 타겟/하이라이트
    val replyNoticeState by viewModel.replyNoticeState.collectAsStateWithLifecycle()
    val targetComment by viewModel.selectedTargetComment.collectAsStateWithLifecycle()
    val highlight by viewModel.highlight.collectAsStateWithLifecycle()

    val saveImageState by viewModel.saveState.collectAsStateWithLifecycle()

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
            Column(modifier = Modifier.fillMaxSize()) {
                when (val s = postState) {
                    is ApiState.Loading -> LoadingOverlay()
                    is ApiState.Error -> ErrorScreen(reason = s.message.orEmpty())
                    is ApiState.Success -> {
                        val post = s.data
                        post?.let {
                            // 제목 동기화
                            viewModel.setTitle(it.title)

                            // 콘텐츠 (상단 Header/이미지/바/댓글목록으로 분리)
                            CommunityDetailView(
                                state = listState,
                                post = it,
                                commentList = commentListState,
                                targetComment = targetComment,
                                highlight = highlight,
                                isWriter = writerState,
                                hasLiked = hasLikedState,
                                bottomPadding = with(density) { commentBoxHeightPx.intValue.toDp() },
                                onPostModifyClick = onPostModifyClick,
                                onPostDeleteClick = viewModel::openPostDeleteDialog,
                                onDeleteCommentClick = viewModel::openCommentDeleteDialog,
                                onDeleteReplyClick = viewModel::openReplyDeleteDialog,
                                onLikeClick = viewModel::setLike,
                                onReplyCallback = { comment ->
                                    viewModel.startReplyTo(commentId = comment.id)
                                    viewModel.openReplyNotice(comment = comment)
                                },
                                onImageClick = viewModel::openImageViewerDialog
                            )
                        }
                    }

                    else -> Unit
                }
            }

            if (postState is ApiState.Success) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            commentBoxHeightPx.intValue = coordinates.size.height
                        }
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    BottomCommentInput(
                        userInfo = userInfo,
                        isReply = replyNoticeState,
                        targetComment = targetComment,
                        commentTextState = commentTextState,
                        onAddCommentClick = { comment ->
                            viewModel.addComment(
                                comment = comment,
                                isReply = replyNoticeState
                            )
                        },
                        onCloseReplyNoticeClick = {
                            viewModel.closeReplyNotice()
                        }
                    )
                }
            }

            // 로딩/토스트/네비게이션 사이드 이펙트 분리
            OverlayAndSideEffects(
                likeState = likeState,
                deletePostState = deletePostState,
                saveImageState = saveImageState,
                onDeleteSuccessNavigateUp = onNavigationClick,
                showToast = { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
            )
        }

        // Dialog Hosts (화면 밖)
        DialogHosts(
            dialogState = dialogState,
            dialogState2 = dialogState2,
            dialogState3 = dialogState3,
            dialogImageViewState = dialogImageViewState,
            onDeletePost = viewModel::deletePost,
            onDismissPost = viewModel::closePostDeleteDialog,
            onDeleteComment = viewModel::deleteSelectedComment,
            onDismissComment = viewModel::closeCommentDeleteDialog,
            onDeleteReply = viewModel::deleteSelectedReply,
            onDismissReply = viewModel::closeReplyDeleteDialog,
            onDismissImageViewer = viewModel::closeImageViewerDialog,
            onImageDownloadClick = { imageUrl ->
                viewModel.downloadImageToGallery(
                    url = imageUrl,
                    displayName = "Seoullo_${System.currentTimeMillis()}"
                )
            }
        )
    }
}

@Composable
private fun OverlayAndSideEffects(
    likeState: ApiState<Unit>,
    deletePostState: ApiState<*>,
    saveImageState: ApiState<Uri>,
    onDeleteSuccessNavigateUp: () -> Unit,
    showToast: (String) -> Unit
) {
    // 둘 중 하나라도 로딩이면 오버레이
    val showLoading = (deletePostState is ApiState.Loading) || (likeState is ApiState.Loading)
    if (showLoading) LoadingOverlay()

    // 삭제 결과 처리
    LaunchedEffect(key1 = deletePostState) {
        when (deletePostState) {
            is ApiState.Success<*> -> onDeleteSuccessNavigateUp()
            is ApiState.Error -> showToast(deletePostState.message.orEmpty())
            else -> Unit
        }
    }

    // 토스트
    LaunchedEffect(key1 = likeState) {
        if (likeState is ApiState.Error) {
            showToast(likeState.message.orEmpty())
        }
    }

    val message = stringResource(R.string.image_download_complete)
    LaunchedEffect(key1 = saveImageState) {
        when (saveImageState) {
            is ApiState.Success -> {
                showToast(message)
            }
            is ApiState.Error -> {
                showToast(saveImageState.message.orEmpty())
            }
            else -> Unit
        }
    }
}

@Composable
private fun DialogHosts(
    dialogState: DialogState,
    dialogState2: DialogState,
    dialogState3: DialogState,
    dialogImageViewState: ImageViewerState,
    onDeletePost: () -> Unit,
    onDismissPost: () -> Unit,
    onDeleteComment: () -> Unit,
    onDismissComment: () -> Unit,
    onDeleteReply: () -> Unit,
    onDismissReply: () -> Unit,
    onDismissImageViewer: () -> Unit,
    onImageDownloadClick: (imageUrl: String) -> Unit
) {
    if (dialogState.isDeletePostDialogOpen) {
        DeleteNoticeDialog(
            text = stringResource(R.string.delete_post_dialog_contents),
            onDone = onDeletePost,
            onClose = onDismissPost
        )
    }
    if (dialogState2.isDeleteCommentDialogOpen) {
        DeleteNoticeDialog(
            text = stringResource(R.string.delete_comment_dialog_contents),
            onDone = onDeleteComment,
            onClose = onDismissComment
        )
    }
    if (dialogState3.isDeleteReplyDialogOpen) {
        DeleteNoticeDialog(
            text = stringResource(R.string.delete_reply_dialog_contents),
            onDone = onDeleteReply,
            onClose = onDismissReply
        )
    }
    if (dialogImageViewState.isOpen && !dialogImageViewState.url.isNullOrBlank()) {
        ImageViewerDialog(
            imageUrl = dialogImageViewState.url!!,
            onClose = onDismissImageViewer,
            onImageDownloadClick = { imageUrl -> onImageDownloadClick(imageUrl) }
        )
    }
}

@Composable
fun CommunityDetailView(
    state: LazyListState,
    post: Post,
    commentList: List<Comment>,
    targetComment: Comment?,
    highlight: Highlight,
    isWriter: Boolean,
    hasLiked: Boolean,
    bottomPadding: Dp = 0.dp,
    modifier: Modifier = Modifier,
    onPostModifyClick: (postId: String) -> Unit,
    onPostDeleteClick: () -> Unit,
    onDeleteCommentClick: (commentId: String) -> Unit,
    onDeleteReplyClick: (commentId: String, replyId: String) -> Unit,
    onLikeClick: (postId: String) -> Unit,
    onReplyCallback: (targetComment: Comment) -> Unit,
    onImageClick: (url: String) -> Unit
) {
    // 스크롤 사이드 이펙트는 뷰 본문에서 분리(가독/테스트성 향상)
    ReplyNavigationEffects(
        listState = state,
        targetComment = targetComment,
        commentList = commentList
    )

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
        // 헤더 (작성자 / 시간 / 옵션)
        item {
            PostHeader(
                post = post,
                isWriter = isWriter,
                onModify = { onPostModifyClick(post.id) },
                onDelete = onPostDeleteClick
            )

            Spacer(Modifier.height(9.dp))

            Text(
                text = post.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(7.dp))

            Text(
                text = post.content,
                fontSize = 18.sp
            )

            // 이미지
            PostImages(
                imageUrls = post.imageUrls,
                onImageClick = { url -> onImageClick(url) }
            )

            // 리액션 바
            Spacer(Modifier.height(9.dp))
            ReactionBar(
                hasLiked = hasLiked,
                likeCount = post.likeCount,
                commentCount = post.commentCount,
                onLikeClick = { onLikeClick(post.id) }
            )

            if (commentList.isNotEmpty()) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 7.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }

        // 댓글 리스트
        items(
            items = commentList,
            key = { it.id }
        ) { comment ->
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                CommentList(
                    item = comment,
                    isHighlighted = (comment.id == highlight.id) && (comment.id == targetComment?.id),
                    triggerSeq = highlight.seq,
                    onReplyCallback = onReplyCallback,
                    onDeleteCommentClick = onDeleteCommentClick,
                    onDeleteReplyClick = onDeleteReplyClick
                )
            }
        }
    }
}

@Composable
private fun PostHeader(
    post: Post,
    isWriter: Boolean,
    onModify: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProfileImage(
            imageUrl = post.authorPhotoUrl,
            size = 30.dp
        )

        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        ) {
            Text(
                text = post.authorName,
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
                    modifier = Modifier
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

        if (isWriter) {
            var isDropDownMenuExpanded by remember { mutableStateOf(false) }
            Box {
                IconButton(onClick = { isDropDownMenuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        tint = MaterialTheme.colorScheme.outline,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    modifier = Modifier.wrapContentSize(),
                    expanded = isDropDownMenuExpanded,
                    onDismissRequest = { isDropDownMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.post_modify)) },
                        onClick = {
                            isDropDownMenuExpanded = false
                            onModify()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.post_delete)) },
                        onClick = {
                            isDropDownMenuExpanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PostImages(
    modifier: Modifier = Modifier,
    imageUrls: List<String>,
    onImageClick: (url: String) -> Unit
) {
    if (imageUrls.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { imageUrls.size })

    Spacer(Modifier.height(9.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.height(350.dp)
        ) { index ->
            imageUrls.getOrNull(index % imageUrls.size)?.let { url ->
                GlideImage(
                    modifier = modifier.clickable { onImageClick(url) },
                    imageModel = url,
                    contentScale = ContentScale.FillBounds,
                    loading = {
                        Box(Modifier.fillMaxSize()) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    },
                    failure = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

    if (imageUrls.size > 1) {
        Spacer(Modifier.height(6.dp))
        PagerIndicator(
            modifier = Modifier.fillMaxWidth(),
            count = imageUrls.size,
            dotSize = 9.dp,
            spacedBy = 4.dp,
            currentPage = pagerState.currentPage % imageUrls.size,
            selectedColor = MaterialTheme.colorScheme.primary,
            unSelectedColor = Color.LightGray,
            dotAlignment = Alignment.Center
        )
    }
}

@Composable
private fun ReactionBar(
    hasLiked: Boolean,
    likeCount: Int,
    commentCount: Int,
    onLikeClick: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onLikeClick) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (hasLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                    tint = if (hasLiked) Color.Red else MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .size(18.dp)
                        .padding(end = 4.dp),
                    contentDescription = null
                )
                Text(
                    text = likeCount.toString(),
                    style = TextStyle(color = MaterialTheme.colorScheme.outline)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Icon(
            imageVector = Icons.Outlined.ModeComment,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .size(18.dp)
                .padding(end = 4.dp),
            contentDescription = null
        )
        Text(
            text = commentCount.toString(),
            style = TextStyle(color = MaterialTheme.colorScheme.outline)
        )
    }
}

// 댓글 입력 바 (하단 고정)
@Composable
private fun BottomCommentInput(
    userInfo: User,
    isReply: Boolean,
    targetComment: Comment?,
    commentTextState: androidx.compose.foundation.text.input.TextFieldState,
    onAddCommentClick: (String) -> Unit,
    onCloseReplyNoticeClick: () -> Unit,
) {
    // 기존 CommentTextField 재사용(강결합 해소: API만 노출)
    CommentTextField(
        userInfo = userInfo,
        commentTextState = commentTextState,
        isReply = isReply,
        targetComment = targetComment,
        onAddCommentClick = { comment -> onAddCommentClick(comment) },
        onCloseReplyNoticeClick = { onCloseReplyNoticeClick() }
    )
}

// 스크롤 관련 사이드 이펙트만 집중 처리 (SRP)
@Composable
private fun ReplyNavigationEffects(
    listState: LazyListState,
    targetComment: Comment?,
    commentList: List<Comment>,
    firstCommentIndex: Int = 1,   // 댓글이 시작되는 LazyColumn 인덱스
) {
    var previousSize by remember { mutableIntStateOf(commentList.size) }

    // 새 댓글 추가 시 하단 스크롤
    LaunchedEffect(key1 = commentList.size) {
        if (commentList.size > previousSize) {
            listState.animateScrollToItem(commentList.size)
        }
        previousSize = commentList.size
    }

    // 답글 달기 시도 시 특정 타겟 댓글 위치로 스크롤
    val targetId by rememberUpdatedState(targetComment?.id)
    LaunchedEffect(
        key1 = targetId,
        key2 = commentList.size
    ) {
        val id = targetId ?: return@LaunchedEffect
        val idxInComments = commentList.indexOfFirst { it.id == id }
        if (idxInComments < 0) return@LaunchedEffect

        val targetIndex = firstCommentIndex + idxInComments

        // 1) 먼저 해당 아이템이 구성될 때까지 대기
        snapshotFlow { listState.layoutInfo.totalItemsCount }
            .first { it > targetIndex }

        // 2) 일단 그 아이템이 보이도록 스냅 이동
        listState.scrollToItem(targetIndex, 0)

        // 3) 실제 레이아웃 값으로 '딱 맨 위' 정렬 보정
        awaitFrame()    // 레이아웃 한 프레임 기다림
        val info = listState.layoutInfo
        val itemInfo =
            info.visibleItemsInfo.firstOrNull { it.index == targetIndex } ?: return@LaunchedEffect

        val dyToTop = (itemInfo.offset - info.viewportStartOffset).toFloat()
        if (dyToTop != 0f) {
            listState.animateScrollBy(dyToTop)
        }
    }
}