package com.app.seoullo_new.view.main.community

import androidx.lifecycle.viewModelScope
import com.app.domain.model.common.ApiState
import com.app.domain.model.community.Comment
import com.app.domain.model.community.Post
import com.app.domain.usecase.community.AddCommentUseCase
import com.app.domain.usecase.community.AddPostUseCase
import com.app.domain.usecase.community.LikePostUseCase
import com.app.domain.usecase.community.ObserveCommentsUseCase
import com.app.domain.usecase.community.ObservePostsUseCase
import com.app.domain.usecase.user.SelectUserUseCase
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.view.base.BaseViewModel2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val selectUserUseCase: SelectUserUseCase,
    private val addPostUseCase: AddPostUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val observePostsUseCase: ObservePostsUseCase,
    private val observeCommentsUseCase: ObserveCommentsUseCase,
    private val likePostUseCase: LikePostUseCase
) : BaseViewModel2(dispatcherProvider) {

    val posts: StateFlow<ApiState<List<Post>>> =
        observePostsUseCase()
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ApiState.Loading()
            )

//    init {
//        a()
//    }

//    fun a() {
//        onIO {
//            val user = selectUserUseCase()
//                .filter { it.isNotEmpty() }
//                .map { it.first() }
//                .firstOrNull() ?: run { return@onIO }
//
//            val postId1 = addPostUseCase(
//                user = user,
//                title = "첫 번째 글입니다",
//                content = "Firebase + Clean Architecture 게시판 샘플 🎉",
//                images = emptyList() // ByteArray 목록 (필요 시 이미지 넣기)
//            )
//            val postId2 = addPostUseCase(
//                user = user,
//                title = "두 번째 글이에요",
//                content = "이미지는 Storage에 올리고 URL을 보관합니다.",
//                images = emptyList()
//            )
//
//            addCommentUseCase(postId1, user, "와! 잘 보입니다 👀")
//            addCommentUseCase(postId1, user, "실시간으로 댓글이 갱신돼요.")
//            addCommentUseCase(postId2, user, "두 번째 글에도 댓글 달아봅니다.")
//
//            likePostUseCase(postId1, like = true)
//        }
//    }
}