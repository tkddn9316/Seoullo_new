package com.app.seoullo_new.utils

import com.app.domain.model.common.BaseModel
import javax.annotation.concurrent.Immutable

@Immutable
data class PostOption(
    val id: String
) : BaseModel()
