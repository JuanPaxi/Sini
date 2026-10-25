package com.juanpaxi.sini.core.database.dao

import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.testing.TestPager
import com.google.common.truth.Truth.assertThat
import com.juanpaxi.sini.core.database.model.MovieEntity
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.uuid.Uuid

internal class MovieDaoTest : DatabaseTest() {
    @Test
    fun upsertAll_insertsMultipleMovies() =
        runTest {
            val movies =
                listOf(
                    testMovieEntity(position = 0),
                    testMovieEntity(position = 1),
                    testMovieEntity(position = 2),
                )
            movieDao.upsertAll(movies)
            val result = movieDao.moviesInPositionRange(0, 2)
            assertThat(result).isEqualTo(movies)
        }

    @Test
    fun upsertAll_updatesExistingMovie() =
        runTest {
            val movie = testMovieEntity()
            movieDao.upsertAll(listOf(movie))
            val updatedMovie = movie.copy(title = "Updated Movie")
            movieDao.upsertAll(listOf(updatedMovie))
            val movies = movieDao.moviesInPositionRange(0, 0)
            assertThat(movies).isEqualTo(listOf(updatedMovie))
        }

    @Test
    fun moviesInPositionRange_returnsMoviesWithinRangeOrderedByPosition() =
        runTest {
            val movies =
                listOf(
                    testMovieEntity(position = 3, title = "Movie 3"),
                    testMovieEntity(position = 1, title = "Movie 1"),
                    testMovieEntity(position = 0, title = "Movie 0"),
                    testMovieEntity(position = 2, title = "Movie 2"),
                )
            movieDao.upsertAll(movies)
            val result = movieDao.moviesInPositionRange(1, 2)
            assertThat(result).isEqualTo(listOf(movies[1], movies[3]))
        }

    @Test
    fun deleteMovies_deletesMoviesWithinRange() =
        runTest {
            val movies =
                listOf(
                    testMovieEntity(position = 0, title = "Movie 0"),
                    testMovieEntity(position = 1, title = "Movie 1"),
                    testMovieEntity(position = 2, title = "Movie 2"),
                    testMovieEntity(position = 3, title = "Movie 3"),
                )
            movieDao.upsertAll(movies)
            movieDao.deleteMovies(1, 2)
            val result = movieDao.moviesInPositionRange(0, 3)
            assertThat(result).isEqualTo(listOf(movies[0], movies[3]))
        }

    @Test
    fun pagingSource_returnsMoviesOrderedByPosition() =
        runTest {
            val movies =
                listOf(
                    testMovieEntity(position = 3, title = "Movie 3"),
                    testMovieEntity(position = 1, title = "Movie 1"),
                    testMovieEntity(position = 0, title = "Movie 0"),
                    testMovieEntity(position = 2, title = "Movie 2"),
                )
            movieDao.upsertAll(movies)
            val pager =
                TestPager(
                    config =
                        PagingConfig(
                            pageSize = 2,
                            initialLoadSize = movies.size,
                        ),
                    pagingSource = movieDao.pagingSource(),
                )
            val result = pager.refresh()
            assertThat(result).isInstanceOf(Page::class.java)
            val page = result as Page
            assertThat(page.data).isEqualTo(movies.sortedBy { it.position })
        }

    @Test
    fun pagingSource_appendLoadsNextMovies() =
        runTest {
            val movies =
                (0 until 6).map { position ->
                    testMovieEntity(
                        position = position,
                        title = "Movie $position",
                    )
                }
            movieDao.upsertAll(movies)
            val pager =
                TestPager(
                    config =
                        PagingConfig(
                            pageSize = 2,
                            initialLoadSize = 2,
                        ),
                    pagingSource = movieDao.pagingSource(),
                )
            val initialPage = pager.refresh()
            assertThat(initialPage).isInstanceOf(Page::class.java)
            assertThat((initialPage as Page).data).isEqualTo(movies.subList(0, 2))

            val nextPage = pager.append()
            assertThat(nextPage).isInstanceOf(Page::class.java)
            assertThat((nextPage as Page).data).isEqualTo(movies.subList(2, 4))
        }

    @Test
    fun pagingSource_returnsEmptyPageWhenDatabaseIsEmpty() =
        runTest {
            val pager =
                TestPager(
                    config =
                        PagingConfig(
                            pageSize = 20,
                            initialLoadSize = 20,
                        ),
                    pagingSource = movieDao.pagingSource(),
                )
            val result = pager.refresh()
            assertThat(result).isInstanceOf(Page::class.java)
            val page = result as Page
            assertThat(page.data).isEmpty()
        }

    @Test
    fun countMovies_returnsCorrectCount() =
        runTest {
            val movies =
                listOf(
                    testMovieEntity(position = 0),
                    testMovieEntity(position = 1),
                    testMovieEntity(position = 2),
                )
            assertThat(movieDao.countMovies()).isEqualTo(0)
            movieDao.upsertAll(movies)
            assertThat(movieDao.countMovies()).isEqualTo(3)
            movieDao.deleteMovies(1, 2)
            assertThat(movieDao.countMovies()).isEqualTo(1)
        }
}

fun testMovieEntity(
    localId: Uuid = Uuid.random(),
    tmdbId: Int = 1,
    position: Int = 0,
    title: String = "Movie 1",
) = MovieEntity(
    localId = localId,
    tmdbId = tmdbId,
    position = position,
    title = title,
    posterPath = null,
    voteAverage = 0.0,
)
