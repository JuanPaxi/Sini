package com.juanpaxi.sini.core.data.testdoubles

import androidx.paging.PagingSource
import com.juanpaxi.sini.core.database.dao.MovieDao
import com.juanpaxi.sini.core.database.model.MovieEntity

class TestMovieDao : MovieDao {
    private val movies = mutableListOf<MovieEntity>()

    override suspend fun upsertAll(movies: List<MovieEntity>) {
        movies.forEach { movie ->
            this.movies.removeAll { it.localId == movie.localId }
            this.movies.add(movie)
        }
    }

    override fun pagingSource(): PagingSource<Int, MovieEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun moviesInPositionRange(
        startPosition: Int,
        endPosition: Int,
    ): List<MovieEntity> =
        movies
            .filter { it.position in startPosition..endPosition }
            .sortedBy { it.position }

    override suspend fun deleteMovies(
        startPosition: Int,
        endPosition: Int,
    ) {
        movies.removeAll { movie ->
            movie.position in startPosition..endPosition
        }
    }

    override suspend fun countMovies(): Int = movies.size
}
