package com.app.seoullo_new.utils

sealed class LoginState(val _state: Boolean?) {
    object loading: LoginState(_state = null)
    data class IsUser(val state: Boolean) : LoginState(_state = state)
}
