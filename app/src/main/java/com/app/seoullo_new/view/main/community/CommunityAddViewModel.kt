package com.app.seoullo_new.view.main.community

import com.app.domain.model.common.ApiState
import com.app.domain.usecase.community.AddPostUseCase
import com.app.domain.usecase.user.SelectUserUseCase
import com.app.seoullo_new.di.DispatcherProvider
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
    dispatcherProvider: DispatcherProvider,
    private val selectUserUseCase: SelectUserUseCase,
    private val addPostUseCase: AddPostUseCase
) : BaseViewModel2(dispatcherProvider) {

    private val _addPostState = MutableStateFlow<ApiState<String>>(ApiState.Initial())
    val addPostState = _addPostState.asStateFlow()

    fun addPost(
        title: String,
        content: String,
        images: List<ByteArray>
    ) {
        onIO {
            val user = selectUserUseCase()
                .filter { it.isNotEmpty() }
                .map { it.first() }
                .firstOrNull() ?: run { return@onIO }

            addPostUseCase(
                user = user,
                title = title,
                content = content,
                images = images
            )
                .flowOn(Dispatchers.IO)
                .collect { state ->
                    _addPostState.value = state
                }
        }
    }
}