package com.app.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.app.data.utils.Logging
import com.app.domain.repository.TodayWatchedListRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ClearWatchedListWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: TodayWatchedListRepository
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            repository.clearWatchedList()
            Logging.e("ClearWatchedListWorker - DB clear")
            Result.success()
        } catch (e: Exception) {
            Logging.e("ClearWatchedListWorker - DB clear failure: ${e.message}")
            Result.failure()
        }
    }
}