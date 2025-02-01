package com.app.data.source

import com.app.data.api.ApiInterface
import com.app.data.model.AutoCompleteRequestDTO
import com.app.data.model.AutoCompleteResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthCompleteDataSourceImpl @Inject constructor(private val apiInterface: ApiInterface) :
    AuthCompleteDataSource {

    override fun getAuthComplete(
        apiKey: String,
        autoCompleteRequestDTO: AutoCompleteRequestDTO
    ): Flow<AutoCompleteResponseDTO> {
        return flow {
            emit(
                apiInterface.getAuthComplete(
                    apiKey = apiKey,
                    autoCompleteRequestDTO = autoCompleteRequestDTO
                )
            )
        }
    }
}