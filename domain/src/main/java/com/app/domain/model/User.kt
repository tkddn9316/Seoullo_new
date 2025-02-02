package com.app.domain.model

import com.app.domain.model.common.BaseModel

data class User(
    val index: Int = 0,
    var auto: String = "N",  // 자동 로그인 여부
    val name: String,
    val email: String,
    val photoUrl: String
) : BaseModel()
