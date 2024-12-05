package com.app.seoullo_new.view.base

import androidx.lifecycle.viewModelScope
import com.app.seoullo_new.di.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 통신 관련 BaseViewModel
 */
open class BaseViewModel2(dispatcherProvider: DispatcherProvider) : BaseViewModel(),
    DispatcherProvider by dispatcherProvider {

    val loadingState = MutableStateFlow(false)
    val errorMessage = MutableStateFlow<String?>(null)

    /** viewModelScope Main Thread */
    inline fun BaseViewModel2.onMain(
        crossinline body: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch {
        loading.value = true
        body(this)
        loading.value = false
    }

    /** viewModelScope IO Thread */
    inline fun BaseViewModel2.onIO(
        crossinline body: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(io) {
        withContext(Dispatchers.Main) { loading.value = true }
        body(this)
        withContext(Dispatchers.Main) { loading.value = false }
    }

    /** viewModelScope Default */
    inline fun BaseViewModel2.onDefault(
        crossinline body: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(default) {
        withContext(Dispatchers.Main) { loading.value = true }
        body(this)
        withContext(Dispatchers.Main) { loading.value = false }
    }

    fun resetErrorMessage() {
        errorMessage.value = null
    }
}