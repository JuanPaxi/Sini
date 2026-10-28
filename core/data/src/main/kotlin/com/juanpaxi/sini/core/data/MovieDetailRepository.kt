package com.juanpaxi.sini.core.data

import com.juanpaxi.sini.core.model.data.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MovieDetailRepository {
    suspend fun getMovieDetail(movieId: Int): MovieDetail

    suspend fun setFavorite(
        movieId: Int,
        isFavorite: Boolean,
    )

    fun observeFavorites(): Flow<List<MovieDetail>>
}
