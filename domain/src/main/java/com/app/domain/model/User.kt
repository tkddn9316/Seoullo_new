package com.app.domain.model

data class User(
    val index: Int,
    var auto: String = "N",  // 자동 로그인 여부
    val name: String,
    val email: String,
    val photoUrl: String
)
