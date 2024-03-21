package com.app.seoullo_new.view

import com.app.domain.model.TourInfo
import com.app.domain.usecase.tourInfo.GetTourInfoUseCase
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.base.BaseViewModel
import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.utils.Constants.ContentTypeId
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.utils.CheckingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class TourListViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val getTourInfoUseCase: GetTourInfoUseCase,
    private val checkingManager: CheckingManager
) : BaseViewModel(dispatcherProvider) {
    private val _tourInfoListResult = MutableStateFlow<List<TourInfo>>(emptyList())
    val tourInfoListResult = _tourInfoListResult.asStateFlow()

    init {
        // 위치 퍼미션 체크
//        checkingManager.
        // true 시 getTourInfo
    }

    fun getTourInfo() {
        onIO {
            getTourInfoUseCase(
                BuildConfig.TOUR_API_KEY,
                ContentTypeId.RESTAURANT.type,
                "126.559888",
                "37.488718",
                10000
            )
                .flowOn(Dispatchers.IO)
                .catch {
                    Logging.e("실패!!!!!!!!!!!!!!!!!!!!!!!!")
                }
                .collect { test ->
                    test.forEach {
                        Logging.e(it.toString())
                    }
                    _tourInfoListResult.value = test
                }
        }
    }
}