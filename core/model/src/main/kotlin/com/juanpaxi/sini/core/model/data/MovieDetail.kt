package com.juanpaxi.sini.core.model.data

import kotlinx.datetime.LocalDate

data class MovieDetail(
    val id: Int,
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
