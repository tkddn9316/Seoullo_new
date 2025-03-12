package com.app.domain.model

import com.app.domain.model.common.BaseModel

data class User(
    val index: Int = 0,
    val name: String,
    val email: String,
    val tokenId: String,
    val photoUrl: String
) : BaseModel()
