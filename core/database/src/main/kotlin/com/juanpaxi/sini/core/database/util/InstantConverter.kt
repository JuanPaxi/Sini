package com.juanpaxi.sini.core.database.util

import androidx.room3.ColumnTypeConverter
import kotlin.time.Instant

internal class InstantConverter {
    @ColumnTypeConverter
    fun longToInstant(value: Long?): Instant? = value?.let(Instant::fromEpochMilliseconds)

    @ColumnTypeConverter
    fun instantToLong(instant: Instant?): Long? = instant?.toEpochMilliseconds()
}
