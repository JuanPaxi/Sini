package com.juanpaxi.sini.core.database.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlin.time.Instant

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey
    val collection: String,
    val nextKey: Int?,
    val lastUpdated: Instant,
)
