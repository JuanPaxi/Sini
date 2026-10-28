package com.juanpaxi.sini.core.data.repository

import com.juanpaxi.sini.core.data.UserDataRepository
import com.juanpaxi.sini.core.datastore.SiniPreferencesDataSource
import com.juanpaxi.sini.core.model.data.DarkThemeConfig
import com.juanpaxi.sini.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OfflineFirstUserDataRepository
    @Inject
    constructor(
        private val siniPreferencesDataSource: SiniPreferencesDataSource,
    ) : UserDataRepository {
        override val userData: Flow<UserData> = siniPreferencesDataSource.userData

        override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
            siniPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
        }
    }
