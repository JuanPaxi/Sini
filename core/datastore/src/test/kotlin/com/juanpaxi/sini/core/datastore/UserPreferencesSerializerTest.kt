package com.juanpaxi.sini.core.datastore

import androidx.datastore.core.CorruptionException
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class UserPreferencesSerializerTest {
    private val userPreferencesSerializer = UserPreferencesSerializer()

    @Test
    fun defaultUserPreferences_isEmpty() {
        assertThat(
            userPreferences {
                // Default value
            },
        ).isEqualTo(
            userPreferencesSerializer.defaultValue,
        )
    }

    @Test
    fun writingAndReadingUserPreferences_outputsCorrectValue() =
        runTest {
            val expectedUserPreferences =
                userPreferences {
                    darkThemeConfig = DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                }

            val outputStream = ByteArrayOutputStream()
            userPreferencesSerializer.writeTo(
                t = expectedUserPreferences,
                output = outputStream,
            )
            val inputStream = ByteArrayInputStream(outputStream.toByteArray())
            val actualUserPreferences = userPreferencesSerializer.readFrom(inputStream)
            assertThat(actualUserPreferences).isEqualTo(expectedUserPreferences)
        }

    @Test(expected = CorruptionException::class)
    fun readingInvalidUserPreferences_throwsCorruptionException() =
        runTest {
            userPreferencesSerializer.readFrom(ByteArrayInputStream(byteArrayOf(0)))
        }
}
