package com.juanpaxi.sini.core.data.repository

import com.google.common.truth.Truth
import com.juanpaxi.sini.core.data.testdoubles.TestMovieDao
import com.juanpaxi.sini.core.data.testdoubles.TestSiniNetworkDataSource
import com.juanpaxi.sini.core.data.testdoubles.TestTransactionRunner
import com.juanpaxi.sini.core.database.model.MovieEntity
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.uuid.Uuid

class MovieSynchronizerTest {
    private lateinit var networkDataSource: TestSiniNetworkDataSource
    private lateinit var movieDao: TestMovieDao
    private lateinit var subject: MovieSynchronizer
    private lateinit var transactionRunner: TestTransactionRunner

    @Before
    fun setup() {
        movieDao = TestMovieDao()
        networkDataSource = TestSiniNetworkDataSource()
        transactionRunner = TestTransactionRunner()
        subject =
            MovieSynchronizer(
                networkDataSource = networkDataSource,
                transactionRunner = transactionRunner,
                movieDao = movieDao,
            )
    }

    @Test
    fun syncInsertsMoviesWhenDatabaseIsEmpty() =
        runTest {
            subject.sync(1)
            val movies = movieDao.moviesInPositionRange(0, 19)
            Truth.assertThat(movies).hasSize(20)
            Truth.assertThat(movies.map { it.position }).containsExactlyElementsIn(0..19)
        }

    @Test
    fun syncPreservesLocalIdsWhenPageIsFull() =
        runTest {
            subject.sync(1)
            val existingMovies = movieDao.moviesInPositionRange(0, 19)
            val existingLocalIds = existingMovies.map { it.localId }
            subject.sync(1)
            val updatedMovies = movieDao.moviesInPositionRange(0, 19)
            Truth.assertThat(updatedMovies).hasSize(20)
            Truth
                .assertThat(updatedMovies.map { it.localId })
                .containsExactlyElementsIn(existingLocalIds)
        }

    @Test
    fun syncReplacesMoviesWhenPageIsIncomplete() =
        runTest {
            val existingMovie =
                MovieEntity(
                    localId = Uuid.Companion.random(),
                    tmdbId = 999,
                    position = 0,
                    title = "Old Movie",
                    posterPath = null,
                    voteAverage = 5.0,
                )
            movieDao.upsertAll(listOf(existingMovie))
            subject.sync(1)
            val movies = movieDao.moviesInPositionRange(0, 19)
            Truth.assertThat(movies).hasSize(20)
            Truth.assertThat(movies.map { it.localId }).doesNotContain(existingMovie.localId)
            Truth.assertThat(movies.map { it.tmdbId }).doesNotContain(existingMovie.tmdbId)
        }

    @Test
    fun syncDoesNotModifyOtherPages() =
        runTest {
            subject.sync(1)
            val firstPageBefore = movieDao.moviesInPositionRange(0, 19)
            subject.sync(2)
            val firstPageAfter = movieDao.moviesInPositionRange(0, 19)
            Truth.assertThat(firstPageAfter).containsExactlyElementsIn(firstPageBefore)
        }

    @Test
    fun syncPropagatesNetworkErrorWithoutModifyingMovies() =
        runTest {
            subject.sync(1)
            val moviesBefore = movieDao.moviesInPositionRange(0, 19)
            networkDataSource.exception = IOException("Network error")
            val exception =
                try {
                    subject.sync(1)
                    null
                } catch (e: IOException) {
                    e
                }
            Truth.assertThat(exception).isNotNull()
            Truth.assertThat(exception).hasMessageThat().isEqualTo("Network error")
            val moviesAfter = movieDao.moviesInPositionRange(0, 19)
            Truth.assertThat(moviesAfter).containsExactlyElementsIn(moviesBefore)
        }

    @Test
    fun syncReturnsTotalPages() =
        runTest {
            val totalPages = subject.sync(1)
            Truth.assertThat(totalPages).isEqualTo(1001)
        }
}
