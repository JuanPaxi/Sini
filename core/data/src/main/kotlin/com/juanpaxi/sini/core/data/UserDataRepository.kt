package com.juanpaxi.sini.core.data

import com.juanpaxi.sini.core.model.data.DarkThemeConfig
import com.juanpaxi.sini.core.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)
}
