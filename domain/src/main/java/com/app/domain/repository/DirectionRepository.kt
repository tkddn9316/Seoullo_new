package com.app.domain.repository

import com.app.domain.model.Direction
import kotlinx.coroutines.flow.Flow

interface DirectionRepository {
    fun getDirection(
        destination: String,
        starting: String,
        languageCode: String,
        apiKey: String
    ): Flow<Direction>
}