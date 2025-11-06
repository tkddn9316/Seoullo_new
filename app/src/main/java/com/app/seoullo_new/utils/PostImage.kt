package com.app.seoullo_new.utils

import android.net.Uri
import java.util.UUID

data class PostImage(
    val id: String,
    val uri: Uri? = null,
    val url: String? = null,
    val isRemote: Boolean
)
