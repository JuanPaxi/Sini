package com.juanpaxi.sini.core.model.data

enum class DarkThemeConfig {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK,
}

data class UserData(
    val darkThemeConfig: DarkThemeConfig,
)
