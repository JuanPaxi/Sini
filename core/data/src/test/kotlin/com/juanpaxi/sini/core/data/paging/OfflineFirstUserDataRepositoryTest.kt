package com.juanpaxi.sini.core.data.paging

import com.google.common.truth.Truth.assertThat
import com.juanpaxi.sini.core.data.repository.OfflineFirstUserDataRepository
import com.juanpaxi.sini.core.datastore.SiniPreferencesDataSource
import com.juanpaxi.sini.core.datastore.UserPreferences
import com.juanpaxi.sini.core.datastore.test.InMemoryDataStore
import com.juanpaxi.sini.core.model.data.DarkThemeConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class OfflineFirstUserDataRepositoryTest {
    private lateinit var subject: OfflineFirstUserDataRepository
    private lateinit var siniPreferencesDataSource: SiniPreferencesDataSource

    @Before
    fun setup() {
        siniPreferencesDataSource =
            SiniPreferencesDataSource(
                userPreferences =
                    InMemoryDataStore(
                        initialValue = UserPreferences.getDefaultInstance(),
                    ),
            )
        subject =
            OfflineFirstUserDataRepository(
                siniPreferencesDataSource = siniPreferencesDataSource,
            )
    }

    @Test
    fun setDarkThemeConfigUpdatesUserDataAndPreferences() =
        runTest {
            subject.setDarkThemeConfig(DarkThemeConfig.DARK)
            val repositoryTheme =
                subject.userData
                    .map { it.darkThemeConfig }
                    .first()
            val preferencesTheme =
                siniPreferencesDataSource.userData
                    .map { it.darkThemeConfig }
                    .first()
            assertThat(repositoryTheme).isEqualTo(DarkThemeConfig.DARK)
            assertThat(preferencesTheme).isEqualTo(DarkThemeConfig.DARK)
        }
}
