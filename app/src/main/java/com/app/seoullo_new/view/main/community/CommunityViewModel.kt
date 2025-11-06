package com.app.seoullo_new.view.main.community

import androidx.lifecycle.viewModelScope
import com.app.domain.model.common.ApiState
import com.app.domain.model.community.Post
import com.app.domain.usecase.community.ObservePostsUseCase
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.view.base.BaseViewModel2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    observePostsUseCase: ObservePostsUseCase
) : BaseViewModel2(dispatcherProvider) {
    private val _hasFirstPostLoaded = MutableStateFlow(false)
    val hasFirstPostLoaded: StateFlow<Boolean> = _hasFirstPostLoaded.asStateFlow()

    val posts: StateFlow<ApiState<List<Post>>> =
        observePostsUseCase.getPostList().distinctUntilChanged().onEach { state ->
                if (!_hasFirstPostLoaded.value && state is ApiState.Success && state.data?.isNotEmpty() == true) {
                    _hasFirstPostLoaded.value = true
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ApiState.Loading()
            )
}