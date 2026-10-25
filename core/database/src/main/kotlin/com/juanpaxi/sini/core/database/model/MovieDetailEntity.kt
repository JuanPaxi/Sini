package com.juanpaxi.sini.core.database.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(
    tableName = "movie_details",
)
data class MovieDetailEntity(
    @PrimaryKey
    val tmdbId: Int,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val overview: String?,
    val voteAverage: Double,
    val releaseDate: LocalDate?,
    val runtime: Int?,
    val genres: List<String>,
    val isFavorite: Boolean,
)
