package com.app.domain.model.theme

enum class Language {
    ENGLISH,
    KOREA;

    companion object {
        fun getByValue(value: Int) = entries.firstOrNull { it.ordinal == value }
    }
}