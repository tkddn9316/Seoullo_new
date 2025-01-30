package com.app.data.source

import com.app.data.model.DustDTO
import kotlinx.coroutines.flow.Flow

interface DustDataSource {
    fun getDustData(
        apiKey: String
    ): Flow<DustDTO>
}