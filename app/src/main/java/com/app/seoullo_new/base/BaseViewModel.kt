package com.app.seoullo_new.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    /** AppBar Title */
    val title = MutableLiveData("")

    /** AppBar BackButton */
    val back = MutableLiveData(true)

    /** AppBar RefreshButton */
    val refresh = MutableLiveData(true)
}