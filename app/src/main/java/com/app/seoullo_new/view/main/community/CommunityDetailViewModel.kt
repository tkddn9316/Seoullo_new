package com.app.seoullo_new.view.main.community

import androidx.lifecycle.SavedStateHandle
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.base.BaseViewModel2
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel2(dispatcherProvider) {
    private val json: String = checkNotNull(savedStateHandle["postId"])

    init {
        Logging.e(json)
    }
}