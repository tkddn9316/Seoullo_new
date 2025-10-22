package com.app.seoullo_new.view.main.community

import com.app.domain.usecase.community.AddCommentUseCase
import com.app.domain.usecase.community.AddPostUseCase
import com.app.domain.usecase.community.LikePostUseCase
import com.app.domain.usecase.community.ObserveCommentsUseCase
import com.app.domain.usecase.community.ObservePostsUseCase
import com.app.domain.usecase.user.SelectUserUseCase
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.base.BaseViewModel2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
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

    // 중복 시딩 방지
    private var seeded = false

    fun setTestData() {
        if (seeded) return
        seeded = true

        onIO {
            val user = selectUserUseCase()
                .filter { it.isNotEmpty() }
                .map { it.first() }
                .firstOrNull() ?: run { return@onIO }

            val hasPosts = observePostsUseCase()
                .map {
                    it.forEach { a->
                        Logging.e(a.title)
                    }
                    it.isNotEmpty()
                }
                .first()
            if (hasPosts) return@onIO

            val postId1 = addPostUseCase(
                user = user,
                title = "첫 번째 글입니다",
                content = "Firebase + Clean Architecture 게시판 샘플 🎉",
                images = emptyList() // ByteArray 목록 (필요 시 이미지 넣기)
            )
            val postId2 = addPostUseCase(
                user = user,
                title = "두 번째 글이에요",
                content = "이미지는 Storage에 올리고 URL을 보관합니다.",
                images = emptyList()
            )

            addCommentUseCase(postId1, user, "와! 잘 보입니다 👀")
            addCommentUseCase(postId1, user, "실시간으로 댓글이 갱신돼요.")
            addCommentUseCase(postId2, user, "두 번째 글에도 댓글 달아봅니다.")

            likePostUseCase(postId1, like = true)
        }
    }
}