package com.app.domain.usecase.todayWatchedList

import com.app.domain.model.Places
import com.app.domain.model.TodayWatchedList
import com.app.domain.repository.TodayWatchedListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayWatchedListUseCase @Inject constructor(private val repository: TodayWatchedListRepository) {
    suspend fun insert(
        data: Places,
        isNearby: Boolean,
        languageCode: String
    ) {
        val todayWatchedList = TodayWatchedList(
            isNearby = if (isNearby) "Y" else "N",
            languageCode = languageCode,
            name = data.name,
            id = data.id,
            contentTypeId = data.contentTypeId,
            displayName = data.displayName,
            address = data.address,
            description = data.description,
            openNow = data.openNow,
            weekdayDescriptions = data.weekdayDescriptions,
            rating = data.rating,
            userRatingCount = data.userRatingCount,
            photoUrl = data.photoUrl
        )
        repository.insertWatchedItem(todayWatchedList)
    }

    fun select(): Flow<List<TodayWatchedList>> {
        return repository.getAllWatchedItems()
    }

    suspend fun delete() {
        // 기존 작동 WorkManager 취소하고 DB DELETE
        repository.onCancelWatchedList()
    }
}