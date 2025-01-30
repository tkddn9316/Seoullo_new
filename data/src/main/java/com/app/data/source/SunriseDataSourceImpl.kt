package com.app.data.source

import com.app.data.api.ApiInterface3
import com.app.data.model.SunriseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SunriseDataSourceImpl @Inject constructor(private val apiInterface: ApiInterface3) :
    SunriseDataSource {

    override fun getSunriseTime(
        currentDate: String,
        apiKey: String
    ): Flow<SunriseDTO> {
        return flow {
            emit(
                apiInterface.getSunriseTime(
                    currentDate = currentDate,
                    apiKey = apiKey
                )
            )
        }
    }
}