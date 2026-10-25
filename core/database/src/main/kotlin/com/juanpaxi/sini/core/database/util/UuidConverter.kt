package com.juanpaxi.sini.core.database.util

import androidx.room3.ColumnTypeConverter
import kotlin.uuid.Uuid

internal class UuidConverter {
    @ColumnTypeConverter
    fun byteArrayToUuid(value: ByteArray?): Uuid? = value?.let(Uuid::fromByteArray)

    @ColumnTypeConverter
    fun uuidToByteArray(value: Uuid?): ByteArray? = value?.toByteArray()
}
