package com.app.data.source

import com.app.data.model.AutoCompleteRequestDTO
import com.app.data.model.AutoCompleteResponseDTO
import kotlinx.coroutines.flow.Flow

interface AuthCompleteDataSource {
    fun getAuthComplete(
        apiKey: String,
        autoCompleteRequestDTO: AutoCompleteRequestDTO
    ): Flow<AutoCompleteResponseDTO>
}