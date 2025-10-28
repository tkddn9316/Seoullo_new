package com.app.seoullo_new.utils

import android.net.Uri
import java.util.UUID

data class PostImage(
    val id: String = UUID.randomUUID().toString(),
    val uri: Uri
)
