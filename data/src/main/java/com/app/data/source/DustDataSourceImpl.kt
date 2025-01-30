package com.app.data.source

import com.app.data.api.ApiInterface3
import com.app.data.model.DustDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DustDataSourceImpl @Inject constructor(private val apiInterface: ApiInterface3) :
    DustDataSource {
    override fun getDustData(apiKey: String): Flow<DustDTO> {
        return flow {
            emit(
                apiInterface.getDustData(
                    apiKey = apiKey
                )
            )
        }
    }
}