package com.app.data.repository

import com.app.data.mapper.mapperToAutoComplete
import com.app.data.mapper.mapperToAutoCompleteRequest
import com.app.data.source.AuthCompleteDataSource
import com.app.domain.model.PlacesAutoComplete
import com.app.domain.model.PlacesAutoCompleteRequest
import com.app.domain.repository.AutoCompleteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AutoCompleteRepositoryImpl @Inject constructor(
    private val autoCompleteDataSource: AuthCompleteDataSource
): AutoCompleteRepository {
    override fun getAutoComplete(
        apiKey: String,
        autoCompleteRequest: PlacesAutoCompleteRequest
    ): Flow<PlacesAutoComplete> {
        return flow {
            autoCompleteDataSource.getAuthComplete(
                apiKey = apiKey,
                autoCompleteRequestDTO = mapperToAutoCompleteRequest(autoCompleteRequest)
            ).collect {
                emit(mapperToAutoComplete(it))
            }
        }
    }
}