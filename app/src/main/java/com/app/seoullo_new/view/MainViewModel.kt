package com.app.seoullo_new.view

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.domain.model.TourInfo
import com.app.domain.usecase.tourInfo.GetTourInfoUseCase
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.base.BaseViewModel
import com.app.seoullo_new.utils.Constants.ContentTypeId
import com.app.seoullo_new.utils.NetworkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTourInfoUseCase: GetTourInfoUseCase,
    private val networkManager: NetworkManager
) : BaseViewModel() {
    private val tourInfoListResult = MutableStateFlow<List<TourInfo>>(emptyList())
    val _tourInfoListResult = tourInfoListResult.asStateFlow()

    fun getTourInfo() {
        viewModelScope.launch {
            runCatching {
                getTourInfoUseCase(
                    BuildConfig.TOUR_API_KEY,
                    ContentTypeId.RESTAURANT.type,
                    "126.559888",
                    "37.488718",
                    10000
                )
                    .flowOn(Dispatchers.IO)
                    .collect { test ->
                        test.forEach {
                            Log.d("AAZZZZZZZZZZ", it.toString())
                        }
                        tourInfoListResult.value = test
                    }
            }.onFailure {
                Log.d("AAZZZZZZZZZZ", "실패!!!!!!!!!!!!!!!!!!!!!!!!")
            }
        }
    }
}