package com.juanpaxi.sini.core.database.model

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "movies",
    indices = [
        Index(value = ["position"], unique = true),
    ],
)
data class MovieEntity(
    @PrimaryKey
    val localId: Uuid,
    val tmdbId: Int,
    val position: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Double,
)
