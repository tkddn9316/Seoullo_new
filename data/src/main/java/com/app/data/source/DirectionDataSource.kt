package com.app.data.source

import com.app.data.model.DirectionResponseDTO
import kotlinx.coroutines.flow.Flow

interface DirectionDataSource {
    fun getDirection(
        destination: String,
        starting: String,
        languageCode: String,
        apiKey: String
    ): Flow<DirectionResponseDTO>
}