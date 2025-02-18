package com.app.data.utils

object Util {
    fun String.addHttps(): String {
        return if (this.startsWith("http://") || this.startsWith("https://")) this
        else "https://$this"
    }
    
    enum class ApiSuccessCode(val code: String) {
        Weather("00")
    }

    fun splitCategory(input: String): Triple<String, String, String> {
        return when {
            input.length >= 9 -> Triple(input.substring(0, 3), input.substring(0, 5), input)
            input.length >= 5 -> Triple(input.substring(0, 3), input.substring(0, 5), "")
            input.length >= 3 -> Triple(input.substring(0, 3), "", "")
            else -> Triple("", "", "")
        }
    }
}