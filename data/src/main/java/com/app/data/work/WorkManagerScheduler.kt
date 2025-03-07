package com.app.data.work

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object WorkManagerScheduler {
    fun scheduleClearWatchedListWork(context: Context) {
        val now = LocalDateTime.now()
        val midnight = now.toLocalDate().plusDays(1).atStartOfDay()
        val initialDelayMillis = Duration.between(now, midnight).toMillis()

        val workRequest = OneTimeWorkRequestBuilder<ClearWatchedListWorker>()
            .setInitialDelay(initialDelayMillis, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "ClearWatchedListWork",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }

    fun cancelWatchedListWork(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("ClearWatchedListWork")
    }
}