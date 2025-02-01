package com.app.domain.repository

import com.app.domain.model.PlacesAutoComplete
import com.app.domain.model.PlacesAutoCompleteRequest
import kotlinx.coroutines.flow.Flow

interface AutoCompleteRepository {
    fun getAutoComplete(
        apiKey: String,
        autoCompleteRequest: PlacesAutoCompleteRequest
    ): Flow<PlacesAutoComplete>
}