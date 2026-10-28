package com.juanpaxi.sini.core.data

import androidx.paging.PagingData
import com.juanpaxi.sini.core.model.data.Movie
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

interface MoviesRepository {
    fun getPopularMovies(): Flow<PagingData<Movie>>

    fun observeLastUpdated(): Flow<Instant?>

    suspend fun countMovies(): Int
}
