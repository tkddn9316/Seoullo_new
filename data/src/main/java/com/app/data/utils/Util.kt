package com.app.data.utils

object Util {
    fun String.addHttps(): String {
        return if (this.startsWith("http://") || this.startsWith("https://")) this
        else "https://$this"
    }
}