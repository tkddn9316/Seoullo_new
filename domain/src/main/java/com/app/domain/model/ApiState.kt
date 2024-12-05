package com.app.domain.model

sealed class ApiState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Initial<T> : ApiState<T>()
    class Success<T>(data: T) : ApiState<T>(data)
    class Error<T>(message: String, data: T? = null) : ApiState<T>(data, message)
    class Loading<T> : ApiState<T>()
}