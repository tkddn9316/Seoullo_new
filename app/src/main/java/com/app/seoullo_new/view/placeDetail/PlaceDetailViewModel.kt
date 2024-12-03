package com.app.seoullo_new.view.placeDetail

import androidx.lifecycle.SavedStateHandle
import com.app.domain.model.Places
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.base.BaseViewModel2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class PlaceDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider
): BaseViewModel2(dispatcherProvider) {
    private val json: String = checkNotNull(savedStateHandle["place"])
    private val place: Places by lazy { Json.decodeFromString<Places>(json) }

    fun getTitle(): String = place.displayName
}