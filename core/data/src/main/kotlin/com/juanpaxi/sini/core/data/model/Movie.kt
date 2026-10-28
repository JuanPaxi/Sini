package com.juanpaxi.sini.core.data.model

import com.juanpaxi.sini.core.database.model.MovieEntity
import com.juanpaxi.sini.core.model.data.Movie
import com.juanpaxi.sini.core.network.model.NetworkMovie
import kotlin.uuid.Uuid

fun NetworkMovie.asEntity(
    position: Int,
    localId: Uuid? = null,
): MovieEntity =
    MovieEntity(
        localId = localId ?: Uuid.random(),
        tmdbId = id,
        position = position,
        title = title,
        posterPath = posterPath,
        voteAverage = voteAverage,
    )

fun MovieEntity.asExternalModel(): Movie =
    Movie(
        id = tmdbId,
        title = title,
        posterPath = posterPath,
        voteAverage = voteAverage,
    )
