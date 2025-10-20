package com.app.seoullo_new.view.placesDetail

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.domain.model.Places
import com.app.domain.model.PlacesDetail
import com.app.domain.model.PlacesDetailGoogle
import com.app.domain.model.PlacesDetailReview
import com.app.domain.model.common.ApiState
import com.app.domain.model.theme.Language
import com.app.domain.usecase.places.GetPlacesDetailGoogleUseCase
import com.app.domain.usecase.places.GetPlacesDetailUseCase
import com.app.domain.usecase.review.PlacesReviewUseCase
import com.app.domain.usecase.todayWatchedList.GetTodayWatchedListUseCase
import com.app.domain.usecase.user.SelectUserUseCase
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.view.base.BaseViewModel2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class PlacesDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    private val getPlacesDetailUseCase: GetPlacesDetailUseCase,
    private val getPlacesDetailGoogleUseCase: GetPlacesDetailGoogleUseCase,
    private val getTodayWatchedListUseCase: GetTodayWatchedListUseCase,
    private val getPlacesReviewUseCase: PlacesReviewUseCase,
    selectUserUseCase: SelectUserUseCase
) : BaseViewModel2(dispatcherProvider) {
    private val json: String = checkNotNull(savedStateHandle["place"])
    private val places: Places by lazy { Json.decodeFromString<Places>(json) }

    private val _placesState = MutableStateFlow(Places())
    val placesState = _placesState.asStateFlow()

    private val _placesDetailState = MutableStateFlow<ApiState<PlacesDetail>>(ApiState.Initial())
    val placesDetailState = _placesDetailState.asStateFlow()

    private val _placesDetailGoogleState = MutableStateFlow<ApiState<PlacesDetailGoogle>>(ApiState.Initial())
    val placesDetailGoogleState = _placesDetailGoogleState.asStateFlow()

    // Nearby 리뷰
    private val _selectedNearbyReview = MutableStateFlow<PlacesDetailGoogle.Review?>(null)
    val selectedNearbyReview = _selectedNearbyReview.asStateFlow()

    // 일반 리뷰
    private val _selectedReview = MutableStateFlow<PlacesDetailReview?>(null)
    val selectedReview = _selectedReview.asStateFlow()

    fun getTitle(): String = places.displayName

    val reviewState: StateFlow<ApiState<List<PlacesDetailReview>>> =
        getPlacesReviewUseCase.getReviews(getTitle())
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ApiState.Loading()
            )

//    private val userInfo: User? = selectUserUseCase()
//        .map { users -> users.firstOrNull() }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = null
//        )
//        .value

    init {
        _placesState.value = places
    }

    fun openNearbyReviewDetailDialog(review: PlacesDetailGoogle.Review) {
        _selectedNearbyReview.value = review
    }

    fun closeNearbyReviewDetailDialog() {
        _selectedNearbyReview.value = null
    }

    fun openReviewDetailDialog(review: PlacesDetailReview) {
        _selectedReview.value = review
    }

    fun closeReviewDetailDialog() {
        _selectedReview.value = null
    }

    fun getPlacesDetail(
        languageCode: Language
    ) {
        if (places.id.isEmpty()) return
        if (_placesDetailState.value !is ApiState.Initial) return

        onIO {
            getPlacesDetailUseCase(
                serviceUrl = if (languageCode == Language.ENGLISH) "EngService2" else "KorService2",
                serviceKey = BuildConfig.TOUR_API_KEY,
                contentId = places.id
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

//    fun summitReview() {
//        onIO {
//            getPlacesReviewUseCase.getReviews(userUseCase)
//        }
//    }

    /** Debug Fake Data */
    fun getFakePlacesDetailGoogle(context: Context) {
        if (_placesDetailGoogleState.value !is ApiState.Initial) return
        _placesDetailGoogleState.value = ApiState.Loading()

        val jsonString = context.assets.open("fake_places_google_data.json").bufferedReader().use { it.readText() }
        val fakePlaces = Json.decodeFromString<PlacesDetailGoogle>(jsonString)
        _placesDetailGoogleState.value = ApiState.Success(fakePlaces)
    }
}