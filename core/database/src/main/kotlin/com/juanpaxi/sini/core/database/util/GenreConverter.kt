package com.juanpaxi.sini.core.database.util

import androidx.room3.ColumnTypeConverter
import kotlinx.serialization.json.Json

internal class GenreConverter {
    private val json = Json

    @ColumnTypeConverter
    fun genresToString(genres: List<String>): String = json.encodeToString(genres)

    @ColumnTypeConverter
    fun stringToGenres(value: String): List<String> = json.decodeFromString(value)
}
