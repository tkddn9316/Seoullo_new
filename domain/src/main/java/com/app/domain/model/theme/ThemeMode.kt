package com.app.domain.model.theme

enum class ThemeMode {
    SYSTEM,
    DARK,
    LIGHT;

    companion object {
        fun getByValue(value: Int) = entries.firstOrNull { it.ordinal == value }
    }
}