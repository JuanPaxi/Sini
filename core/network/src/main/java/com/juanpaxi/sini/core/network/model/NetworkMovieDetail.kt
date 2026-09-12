package com.juanpaxi.sini.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkMovieDetail(
    val id: Int,
    val title: String,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    val overview: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("release_date")
    val releaseDate: String? = null,
    val runtime: Int? = null,
    val genres: List<NetworkGenre>,
)

@Serializable
data class NetworkGenre(
    val id: Int,
    val name: String,
)
