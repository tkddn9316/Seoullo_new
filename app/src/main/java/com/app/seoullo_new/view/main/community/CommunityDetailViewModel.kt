package com.app.seoullo_new.view.main.community

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.domain.model.DeletionResult
import com.app.domain.model.User
import com.app.domain.model.common.ApiState
import com.app.domain.model.community.Comment
import com.app.domain.model.community.Post
import com.app.domain.usecase.community.CommentUseCase
import com.app.domain.usecase.community.LikePostUseCase
import com.app.domain.usecase.community.ObserveCommentsUseCase
import com.app.domain.usecase.community.ObservePostsUseCase
import com.app.domain.usecase.community.PostUseCase
import com.app.domain.usecase.user.SelectUserUseCase
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.base.BaseViewModel2
import com.app.seoullo_new.view.util.DialogState
import com.app.seoullo_new.view.util.Highlight
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    observePostsUseCase: ObservePostsUseCase,
    observeCommentsUseCase: ObserveCommentsUseCase,
    selectUserUseCase: SelectUserUseCase,
    private val auth: FirebaseAuth,
    private val postUseCase: PostUseCase,
    private val commentUseCase: CommentUseCase,
    private val likePostUseCase: LikePostUseCase
) : BaseViewModel2(dispatcherProvider) {
    private val postId: String = checkNotNull(savedStateHandle["postId"])

    private val _deletePostState = MutableStateFlow<ApiState<DeletionResult>>(ApiState.Initial())
    val deletePostState = _deletePostState.asStateFlow()

    // 게시글 삭제 팝업
    private val _dialogState = MutableStateFlow(DialogState())
    val dialogState: StateFlow<DialogState> = _dialogState.asStateFlow()

    fun openPostDeleteDialog() = _dialogState.update { it.copy(isDeletePostDialogOpen = true) }
    fun closePostDeleteDialog() = _dialogState.update { it.copy(isDeletePostDialogOpen = false) }

    // 댓삭 팝업
    private val _dialogState2 = MutableStateFlow(DialogState())
    val dialogState2: StateFlow<DialogState> = _dialogState2.asStateFlow()

    private val _selectedCommentId = MutableStateFlow<String?>(null)

    fun openCommentDeleteDialog(commentId: String) {
        _selectedCommentId.value = commentId
        _dialogState2.value = _dialogState2.value.copy(isDeleteCommentDialogOpen = true)
    }

    fun closeCommentDeleteDialog() {
        _dialogState2.value = _dialogState2.value.copy(isDeleteCommentDialogOpen = false)
        _selectedCommentId.value = null
    }

    fun deleteSelectedComment() {
        _selectedCommentId.value?.let { id ->
            onIO {
                deleteComment(id)
                closeCommentDeleteDialog()
            }
        }
    }

    // 리플 관련
    private val _replyNoticeState = MutableStateFlow(false)
    val replyNoticeState = _replyNoticeState.asStateFlow()
    private val _selectedTargetComment = MutableStateFlow<Comment?>(null)
    val selectedTargetComment = _selectedTargetComment.asStateFlow()

    fun openReplyNotice(comment: Comment) {
        _selectedTargetComment.value = comment
        _replyNoticeState.value = true
    }

    fun closeReplyNotice() {
        _selectedTargetComment.value = null
        _replyNoticeState.value = false
    }

    private val _dialogState3 = MutableStateFlow(DialogState())
    val dialogState3: StateFlow<DialogState> = _dialogState3.asStateFlow()

    private val _selectedReplyId = MutableStateFlow<Pair<String, String>?>(null)

    fun openReplyDeleteDialog(commentId: String, replyId: String) {
        _selectedReplyId.value = Pair(commentId, replyId)
        _dialogState3.value = _dialogState3.value.copy(isDeleteReplyDialogOpen = true)
    }

    fun closeReplyDeleteDialog() {
        _selectedReplyId.value = null
        _dialogState3.value = _dialogState3.value.copy(isDeleteReplyDialogOpen = false)
    }

    fun deleteSelectedReply() {
        _selectedReplyId.value?.let { ids ->
            onIO {
                deleteReply(ids.first, ids.second)
                closeReplyDeleteDialog()
            }
        }
    }

    private val _isWriter = MutableStateFlow(false)
    val isWriter = _isWriter.asStateFlow()

    private val _hasLiked = MutableStateFlow(false)
    val hasLiked: StateFlow<Boolean> = _hasLiked

    val user: StateFlow<User> =
        selectUserUseCase()
            .flowOn(Dispatchers.IO)
            .filter { it.isNotEmpty() }
            .map { it.first() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = User(0, "", "", "", "")
            )

    val post: StateFlow<ApiState<Post>> =
        observePostsUseCase.getPost(postId)
            .onEach { state ->
                if (state is ApiState.Success && state.data != null) {
                    checkIsWriter(state.data!!.authorId)
                    checkIsLike(state.data!!.id)
                }
            }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ApiState.Loading()
            )

    val comments: StateFlow<List<Comment>> =
        observeCommentsUseCase(postId = postId)
            .map { commentList ->
                commentList.map { comment ->
                    comment.copy(
                        isMine = comment.authorId == auth.currentUser?.uid,
                        replyList = comment.replyList.map { reply ->
                            reply.copy(isMine = reply.authorId == auth.currentUser?.uid)
                        }
                    )
                }
            }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // 하이라이트
    private val _highlight = MutableStateFlow(Highlight())
    val highlight: StateFlow<Highlight> = _highlight

    fun startReplyTo(commentId: String) {
        _highlight.update { it.copy(id = commentId, seq = it.seq + 1) }
    }

    fun setTitle(value: String) {
        title.value = value
    }

    // 글쓴이 여부
    private fun checkIsWriter(authorId: String) {
        _isWriter.value = authorId == (auth.currentUser?.uid ?: "")
    }

    // 좋아요 여부
    private suspend fun checkIsLike(postId: String) {
        likePostUseCase.hasLiked(postId = postId, user = user.value).onSuccess {
            _hasLiked.value = it
        }
    }

    // 좋아요 클릭
    fun setLike(postId: String) {
        if (this.postId == postId) {
            onIO {
                if (_hasLiked.value) {
                    likePostUseCase.unlike(postId = postId, user = user.value)
                        .onFailure { e -> Logging.e("좋아요 실패: ${e.message}") }
                } else {
                    likePostUseCase.like(postId = postId, user = user.value)
                        .onFailure { e -> Logging.e("좋아요 실패: ${e.message}") }
                }
                checkIsLike(postId = postId)
            }
        }
    }

    // 댓글 달기
    fun addComment(comment: String, isReply: Boolean) {
        onIO {
            if (isReply) {
                // 답글
                _selectedTargetComment.value?.let { targetComment ->
                    commentUseCase.addReply(
                        postId = postId,
                        commentId = targetComment.id,
                        user = user.value,
                        text = comment
                    ).collect()

                    // 답글 모드 종료
                    closeReplyNotice()
                }
            } else {
                // 댓글
                commentUseCase.addComment(
                    postId = postId,
                    user = user.value,
                    text = comment
                ).collect()
            }
        }
    }

    // 게시글 삭제
    fun deletePost() {
        onIO {
            closePostDeleteDialog()
            postUseCase.deletePost(postId = postId)
                .collect { state ->
                    _deletePostState.value = state
                }
        }
    }

    // 댓글 삭제
    private fun deleteComment(commentId: String) {
        onIO {
            commentUseCase.deleteComment(
                postId = postId,
                commentId = commentId
            ).collect()
        }
    }

    // 답글 삭제
    private fun deleteReply(commentId: String, replyId: String) {
        onIO {
            commentUseCase.deleteReply(
                postId = postId,
                commentId = commentId,
                replyId = replyId
            ).collect()
        }
    }
}