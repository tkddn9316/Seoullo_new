package com.app.seoullo_new.view.placesDetail

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.app.domain.model.Places
import com.app.domain.model.PlacesDetail
import com.app.domain.model.PlacesDetailGoogle
import com.app.domain.model.common.ApiState
import com.app.domain.model.theme.Language
import com.app.domain.usecase.places.GetPlacesDetailGoogleUseCase
import com.app.domain.usecase.places.GetPlacesDetailUseCase
import com.app.domain.usecase.todayWatchedList.GetTodayWatchedListUseCase
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.view.base.BaseViewModel2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class PlacesDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    private val getPlacesDetailUseCase: GetPlacesDetailUseCase,
    private val getPlacesDetailGoogleUseCase: GetPlacesDetailGoogleUseCase,
    private val getTodayWatchedListUseCase: GetTodayWatchedListUseCase
) : BaseViewModel2(dispatcherProvider) {
    private val json: String = checkNotNull(savedStateHandle["place"])
    private val places: Places by lazy { Json.decodeFromString<Places>(json) }

    private val _placesState = MutableStateFlow(Places())
    val placesState = _placesState.asStateFlow()

    private val _placesDetailState = MutableStateFlow<ApiState<PlacesDetail>>(ApiState.Initial())
    val placesDetailState = _placesDetailState.asStateFlow()

    private val _placesDetailGoogleState = MutableStateFlow<ApiState<PlacesDetailGoogle>>(ApiState.Initial())
    val placesDetailGoogleState = _placesDetailGoogleState.asStateFlow()

    private val _selectedReview = MutableStateFlow<PlacesDetailGoogle.Review?>(null)
    val selectedReview = _selectedReview.asStateFlow()
    fun openReviewDetailDialog(review: PlacesDetailGoogle.Review) {
        _selectedReview.value = review
    }
    fun closeReviewDetailDialog() {
        _selectedReview.value = null
    }

    init {
        _placesState.value = places
    }

    fun getTitle(): String = places.displayName

    fun getPlacesDetail(
        languageCode: Language
    ) {
        if (places.id.isEmpty()) return
        if (_placesDetailState.value !is ApiState.Initial) return

        onIO {
            getPlacesDetailUseCase(
                serviceUrl = if (languageCode == Language.ENGLISH) "EngService1" else "KorService1",
                serviceKey = BuildConfig.TOUR_API_KEY,
                contentId = places.id,
                contentTypeId = places.contentTypeId
            )
                .flowOn(Dispatchers.IO)
                .collect { state ->
                    // 상태 업데이트
                    _placesDetailState.value = state
                }
        }
    }

    fun getPlacesDetailGoogle(languageCode: String) {
        if (places.id.isEmpty()) return
        if (_placesDetailGoogleState.value !is ApiState.Initial) return

        onIO {
            getPlacesDetailGoogleUseCase(
                apiKey = BuildConfig.SEOULLO_GOOGLE_MAPS_API_KEY,
                placeId = places.id,
                languageCode = languageCode
            )
                .flowOn(Dispatchers.IO)
                .collect { state ->
                    // 상태 업데이트
                    _placesDetailGoogleState.value = state
                }
        }
    }

    fun insertTodayWatchedList(data: Places, isNearby: Boolean, languageCode: String) {
        onIO {
            getTodayWatchedListUseCase.insert(
                data = data,
                isNearby = isNearby,
                languageCode = languageCode
            )
        }
    }

    /** Debug Fake Data */
    fun getFakePlacesDetailGoogle(context: Context) {
        if (_placesDetailGoogleState.value !is ApiState.Initial) return
        _placesDetailGoogleState.value = ApiState.Loading()

        val jsonString = context.assets.open("fake_places_google_data.json").bufferedReader().use { it.readText() }
        val fakePlaces = Json.decodeFromString<PlacesDetailGoogle>(jsonString)
        _placesDetailGoogleState.value = ApiState.Success(fakePlaces)
    }
}