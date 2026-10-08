package com.juanpaxi.sini.core.model.data

import kotlinx.datetime.LocalDate

data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Double,
    val releaseDate: LocalDate?,
)
