package com.app.data.repository

import com.app.data.mapper.directionMapper
import com.app.data.source.DirectionDataSource
import com.app.domain.model.Direction
import com.app.domain.repository.DirectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DirectionRepositoryImpl @Inject constructor(
    private val directionDataSource: DirectionDataSource
) : DirectionRepository {

    override fun getDirection(
        destination: String,
        starting: String,
        languageCode: String,
        apiKey: String
    ): Flow<Direction> {
        return flow {
            directionDataSource.getDirection(
                destination = destination,
                starting = starting,
                languageCode = languageCode,
                apiKey = apiKey
            ).collect {
                emit(directionMapper(it))
            }
        }
    }
}