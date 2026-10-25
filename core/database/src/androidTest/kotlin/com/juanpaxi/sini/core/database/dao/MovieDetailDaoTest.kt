package com.juanpaxi.sini.core.database.dao

import com.google.common.truth.Truth.assertThat
import com.juanpaxi.sini.core.database.model.MovieDetailEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class MovieDetailDaoTest : DatabaseTest() {
    @Test
    fun upsert_insertsNewMovieDetail() =
        runTest {
            val movieDetail = testMovieDetailEntity(tmdbId = 969681, title = "Movie")
            movieDetailDao.upsert(movieDetail)
            val result = movieDetailDao.movieDetail(movieDetail.tmdbId)
            assertThat(result).isEqualTo(movieDetail)
        }

    @Test
    fun upsert_updatesExistingMovieDetail() =
        runTest {
            val movieDetail = testMovieDetailEntity(tmdbId = 969681, title = "Movie")
            movieDetailDao.upsert(movieDetail)
            val updatedMovieDetail =
                movieDetail.copy(
                    title = "Updated Movie",
                    voteAverage = 8.5,
                    isFavorite = true,
                )
            movieDetailDao.upsert(updatedMovieDetail)
            val result = movieDetailDao.movieDetail(movieDetail.tmdbId)
            assertThat(result).isEqualTo(updatedMovieDetail)
        }

    @Test
    fun movieDetail_returnsNullWhenMovieDoesNotExist() =
        runTest {
            val result = movieDetailDao.movieDetail(tmdbId = 969681)
            assertThat(result).isNull()
        }

    @Test
    fun updateFavorite_updatesFavoriteStatus() =
        runTest {
            val movieDetail = testMovieDetailEntity(969681, "Movie")
            movieDetailDao.upsert(movieDetail)
            movieDetailDao.updateFavorite(movieDetail.tmdbId, true)
            val favoriteMovie = movieDetailDao.movieDetail(movieDetail.tmdbId)
            assertThat(favoriteMovie).isEqualTo(movieDetail.copy(isFavorite = true))
            movieDetailDao.updateFavorite(movieDetail.tmdbId, false)
            val nonFavoriteMovie = movieDetailDao.movieDetail(movieDetail.tmdbId)
            assertThat(nonFavoriteMovie).isEqualTo(movieDetail.copy(isFavorite = false))
        }

    @Test
    fun favorites_returnsFavoriteMoviesOrderedByTitle() =
        runTest {
            val movies =
                listOf(
                    testMovieDetailEntity(tmdbId = 1, title = "Spiderman", isFavorite = true),
                    testMovieDetailEntity(tmdbId = 2, title = "Avatar", isFavorite = true),
                    testMovieDetailEntity(tmdbId = 3, title = "Batman"),
                )
            movieDetailDao.upsert(movies[0])
            movieDetailDao.upsert(movies[1])
            movieDetailDao.upsert(movies[2])
            val result = movieDetailDao.favorites().first()
            assertThat(result).isEqualTo(listOf(movies[1], movies[0]))
        }
}

fun testMovieDetailEntity(
    tmdbId: Int = 1,
    title: String = "Movie",
    isFavorite: Boolean = false,
) = MovieDetailEntity(
    tmdbId = tmdbId,
    title = title,
    posterPath = null,
    backdropPath = null,
    overview = null,
    voteAverage = 0.0,
    releaseDate = null,
    runtime = null,
    genres = emptyList(),
    isFavorite = isFavorite,
)
