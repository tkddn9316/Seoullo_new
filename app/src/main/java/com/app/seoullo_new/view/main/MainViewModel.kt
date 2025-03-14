package com.app.seoullo_new.view.main

import androidx.lifecycle.viewModelScope
import com.app.domain.usecase.user.SelectUserUseCase
import com.app.seoullo_new.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val selectUserUseCase: SelectUserUseCase
) : BaseViewModel() {
    // 구글 프로필 이미지
    private val _profileImageUrl = MutableStateFlow<String?>(null)
    val profileImageUrl = _profileImageUrl.asStateFlow()

    init {
        fetchGoogleProfileImage()
    }

    private fun fetchGoogleProfileImage() {
        viewModelScope.launch {
            selectUserUseCase()
                .flowOn(Dispatchers.IO)
                .filter { it.isNotEmpty() }
                .map { it.first() }
                .collect { user -> _profileImageUrl.value = user.photoUrl }
        }
    }
}