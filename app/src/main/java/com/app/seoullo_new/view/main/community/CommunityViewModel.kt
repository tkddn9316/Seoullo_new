package com.app.seoullo_new.view.main.community

import com.app.seoullo_new.di.DispatcherProvider
import com.app.seoullo_new.view.base.BaseViewModel2
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider
) : BaseViewModel2(dispatcherProvider) {

}