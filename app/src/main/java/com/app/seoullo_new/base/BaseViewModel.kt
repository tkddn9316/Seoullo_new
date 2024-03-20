package com.app.seoullo_new.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.seoullo_new.di.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel(dispatcherProvider: DispatcherProvider) : ViewModel(),
    DispatcherProvider by dispatcherProvider {

    /** AppBar Title */
    val title = MutableLiveData("")

    /** AppBar BackButton */
    val back = MutableLiveData(true)

    /** AppBar RefreshButton */
    val refresh = MutableLiveData(true)
    val loading = MutableLiveData(false)

    /** viewModelScope Main Thread */
    inline fun BaseViewModel.onMain(
        crossinline body: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch {
        loading.value = true
        body(this)
        loading.value = false
    }

    /** viewModelScope IO Thread */
    inline fun BaseViewModel.onIO(
        crossinline body: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(io) {
        withContext(Dispatchers.Main) { loading.value = true }
        body(this)
        withContext(Dispatchers.Main) { loading.value = false }
    }

    /** viewModelScope Default */
    inline fun BaseViewModel.onDefault(
        crossinline body: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(default) {
        withContext(Dispatchers.Main) { loading.value = true }
        body(this)
        withContext(Dispatchers.Main) { loading.value = false }
    }

}