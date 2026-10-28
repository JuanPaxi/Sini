package com.juanpaxi.sini.core.data.model

import com.juanpaxi.sini.core.database.model.MovieDetailEntity
import com.juanpaxi.sini.core.model.data.MovieDetail
import com.juanpaxi.sini.core.network.model.NetworkGenre
import com.juanpaxi.sini.core.network.model.NetworkMovieDetail
import kotlinx.datetime.LocalDate

fun NetworkMovieDetail.asEntity(isFavorite: Boolean): MovieDetailEntity =
    MovieDetailEntity(
        tmdbId = id,
        title = title,
        posterPath = posterPath,
        backdropPath = backdropPath,
        overview = overview,
        voteAverage = voteAverage,
        releaseDate =
            releaseDate
                ?.takeIf(String::isNotBlank)
                ?.let(LocalDate::parse),
        runtime = runtime,
        genres = genres.map(NetworkGenre::name),
        isFavorite = isFavorite,
    )

fun MovieDetailEntity.asExternalModel(): MovieDetail =
    MovieDetail(
        id = tmdbId,
        title = title,
        posterPath = posterPath,
        backdropPath = backdropPath,
        overview = overview,
        voteAverage = voteAverage,
        releaseDate = releaseDate,
        runtime = runtime,
        genres = genres,
        isFavorite = isFavorite,
    )
