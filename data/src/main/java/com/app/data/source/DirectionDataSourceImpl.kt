package com.app.data.source

import com.app.data.api.ApiInterface
import com.app.data.model.DirectionResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DirectionDataSourceImpl @Inject constructor(private val apiInterface: ApiInterface) :
    DirectionDataSource {

    override fun getDirection(
        destination: String,
        starting: String,
        languageCode: String,
        apiKey: String
    ): Flow<DirectionResponseDTO> {
        return flow {
            emit(
                apiInterface.getDirection(
                    destination = destination,
                    starting = starting,
                    languageCode = languageCode,
                    apiKey = apiKey
                )
            )
        }
    }
}