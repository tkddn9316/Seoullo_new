package com.app.data.source

import com.app.data.api.ApiInterface3
import com.app.data.model.SunriseDTO
import com.app.data.utils.SunriseXmlParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SunriseDataSourceImpl @Inject constructor(
    private val apiInterface: ApiInterface3,
    private val parser: SunriseXmlParser
) : SunriseDataSource {

    override fun getSunriseTime(
        currentDate: String,
        apiKey: String
    ): Flow<SunriseDTO> {
        return flow {
            val responseBody = apiInterface.getSunriseTime(
                currentDate = currentDate,
                apiKey = apiKey
            )
            emit(parser.parse(responseBody))
        }
    }
}