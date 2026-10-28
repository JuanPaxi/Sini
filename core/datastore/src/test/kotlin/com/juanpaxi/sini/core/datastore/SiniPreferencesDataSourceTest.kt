package com.juanpaxi.sini.core.datastore

import com.google.common.truth.Truth.assertThat
import com.juanpaxi.sini.core.datastore.test.InMemoryDataStore
import com.juanpaxi.sini.core.model.data.DarkThemeConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SiniPreferencesDataSourceTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: SiniPreferencesDataSource

    @Before
    fun setup() {
        subject =
            SiniPreferencesDataSource(
                InMemoryDataStore(UserPreferences.getDefaultInstance()),
            )
    }

    @Test
    fun userData_returnsDefaultValues() =
        testScope.runTest {
            val userData = subject.userData.first()
            assertThat(userData.darkThemeConfig).isEqualTo(DarkThemeConfig.FOLLOW_SYSTEM)
        }

    @Test
    fun setDarkThemeConfig_followSystem_updatesUserData() =
        testScope.runTest {
            subject.setDarkThemeConfig(DarkThemeConfig.FOLLOW_SYSTEM)
            val userData = subject.userData.first()
            assertThat(userData.darkThemeConfig).isEqualTo(DarkThemeConfig.FOLLOW_SYSTEM)
        }

    @Test
    fun setDarkThemeConfig_light_updatesUserData() =
        testScope.runTest {
            subject.setDarkThemeConfig(DarkThemeConfig.LIGHT)
            val userData = subject.userData.first()
            assertThat(userData.darkThemeConfig).isEqualTo(DarkThemeConfig.LIGHT)
        }

    @Test
    fun setDarkThemeConfig_dark_updatesUserData() =
        testScope.runTest {
            subject.setDarkThemeConfig(DarkThemeConfig.DARK)
            val userData = subject.userData.first()
            assertThat(userData.darkThemeConfig).isEqualTo(DarkThemeConfig.DARK)
        }
}
