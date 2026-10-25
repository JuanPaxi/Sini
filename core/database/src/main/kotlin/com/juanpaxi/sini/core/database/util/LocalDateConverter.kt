package com.juanpaxi.sini.core.database.util

import androidx.room3.ColumnTypeConverter
import kotlinx.datetime.LocalDate

internal class LocalDateConverter {
    @ColumnTypeConverter
    fun stringToLocalDate(value: String?): LocalDate? = value?.let(LocalDate::parse)

    @ColumnTypeConverter
    fun localDateToString(value: LocalDate?): String? = value?.toString()
}
