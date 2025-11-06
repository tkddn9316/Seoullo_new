package com.app.seoullo_new.view.main.community

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.app.domain.model.common.ApiState
import com.app.domain.model.community.Post
import com.app.domain.usecase.community.ObservePostsUseCase
import com.app.domain.usecase.community.PostUseCase
import com.app.domain.usecase.user.SelectUserUseCase
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.PostImage
import com.app.seoullo_new.view.base.BaseViewModel2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CommunityAddViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    private val selectUserUseCase: SelectUserUseCase,
    private val postUseCase: PostUseCase,
    private val observePostsUseCase: ObservePostsUseCase
) : BaseViewModel2(dispatcherProvider) {
    private val postId: String? = savedStateHandle["postId"]
    private val isEdit = postId != null
    private val _isEditState = MutableStateFlow(isEdit)
    val isEditState = _isEditState.asStateFlow()

    private val _postState = MutableStateFlow<Post?>(null)
    val postState = _postState.asStateFlow()

    init {
        if (isEdit && !postId.isNullOrBlank()) {
            onIO {
                // 수정 모드일 때 해당 게시글 데이터 불러오기
                observePostsUseCase.getPost(postId = postId)
                    .collect { state ->
                        when (state) {
                            is ApiState.Success -> {
                                _postState.value = state.data
                            }
                            else -> {}
                        }
                    }
            }
        }
    }

    private val _addPostState = MutableStateFlow<ApiState<String>>(ApiState.Initial())
    val addPostState = _addPostState.asStateFlow()

    private val _addImageList = MutableStateFlow<List<PostImage>>(emptyList())
    val addImageList = _addImageList.asStateFlow()

    fun addPost(
        title: String,
        content: String
    ) {
        onIO {
            val user = selectUserUseCase()
                .filter { it.isNotEmpty() }
                .map { it.first() }
                .firstOrNull() ?: run { return@onIO }
            val sendImageList = _addImageList.value.filter { !it.isRemote }.mapNotNull { it.uri }

            postUseCase.addPost(
                user = user,
                title = title,
                content = content,
                images = sendImageList
            )
                .flowOn(Dispatchers.IO)
                .collect { state ->
                    _addPostState.value = state
                }
        }
    }

    fun updatePost(
        title: String,
        content: String
    ) {
        val id = postId ?: return
        val keptRemote = addImageList.value.filter { it.isRemote }.mapNotNull { it.url }
        val newUris = addImageList.value.filter { !it.isRemote }.mapNotNull { it.uri }

        onIO {
            val user = selectUserUseCase()
                .filter { it.isNotEmpty() }
                .map { it.first() }
                .firstOrNull() ?: run { return@onIO }

            postUseCase.updatePost(
                user = user,
                postId = id,
                title = title,
                content = content,
                keptRemoteUrls = keptRemote,
                newLocalUris = newUris,
                deleteRemoved = true
            )
                .flowOn(Dispatchers.IO)
                .collect { state ->
                    _addPostState.value = state
                }
        }
    }

    fun addImages(uris: List<Uri>) {
        val now = System.currentTimeMillis()
        val appended = uris.mapIndexed { idx, uri ->
            PostImage(id = "local_${now}_$idx", uri = uri, isRemote = false)
        }
        _addImageList.value += appended
    }

    fun removeImage(id: String) {
        _addImageList.value = _addImageList.value.filterNot { it.id == id }
    }

    /** 서버에서 받아온 기존 이미지 URL을 초기 이미지로 세팅 */
    fun setInitialRemoteImages(urls: List<String>) {
        if (urls.isEmpty()) return
        if (_addImageList.value.any { it.isRemote }) return

        val items = urls.map { url -> PostImage(id = url, uri = Uri.parse(url), url = url, isRemote = true) }
        _addImageList.value = items
    }
}