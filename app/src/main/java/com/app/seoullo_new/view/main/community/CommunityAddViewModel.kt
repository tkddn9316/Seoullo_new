package com.app.seoullo_new.view.main.community

import android.net.Uri
import com.app.domain.model.common.ApiState
import com.app.domain.usecase.community.AddPostUseCase
import com.app.domain.usecase.user.SelectUserUseCase
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.Logging
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
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CommunityAddViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val selectUserUseCase: SelectUserUseCase,
    private val addPostUseCase: AddPostUseCase
) : BaseViewModel2(dispatcherProvider) {

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
            val sendImageList = _addImageList.value.map { it.uri }

            addPostUseCase(
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

    fun addImages(uris: List<Uri>) {
        if (uris.isEmpty()) return
        val news = uris.map { PostImage(uri = it) }
        _addImageList.update { it + news }
        Logging.e(_addImageList.value.size.toString())
    }

    fun removeImage(id: String) {
        _addImageList.update { list -> list.filterNot { it.id == id } }
        Logging.e(_addImageList.value.size.toString())
    }
}