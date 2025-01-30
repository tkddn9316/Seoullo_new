package com.app.data.source

import com.app.data.model.SunriseDTO
import kotlinx.coroutines.flow.Flow

interface SunriseDataSource {
    fun getSunriseTime(
        currentDate: String,
        apiKey: String
    ): Flow<SunriseDTO>
}