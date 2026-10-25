package com.juanpaxi.sini.core.database.dao

import com.google.common.truth.Truth.assertThat
import com.juanpaxi.sini.core.database.RoomTransactionRunner
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class RoomTransactionRunnerTest : DatabaseTest() {
    private lateinit var transactionRunner: RoomTransactionRunner

    @Before
    fun setupTransactionRunner() {
        transactionRunner = RoomTransactionRunner(db)
    }

    @Test
    fun run_commitsChangesWhenBlockReturnsUnit() =
        runTest {
            val movie = testMovieEntity(tmdbId = 969681, title = "Movie")
            transactionRunner.run {
                movieDao.upsertAll(listOf(movie))
            }
            val result = movieDao.countMovies()
            assertThat(result).isEqualTo(1)
        }

    @Test
    fun run_returnsBlockResult() =
        runTest {
            val movie = testMovieEntity(tmdbId = 969681, title = "Movie")
            val result =
                transactionRunner.run {
                    movieDao.upsertAll(listOf(movie))
                    movieDao.countMovies()
                }
            assertThat(result).isEqualTo(1)
        }

    @Test
    fun run_rollsBackChangesWhenBlockFails() =
        runTest {
            val movie = testMovieEntity(tmdbId = 969681, title = "Movie")
            val result =
                runCatching {
                    transactionRunner.run {
                        movieDao.upsertAll(listOf(movie))
                        throw IllegalStateException("Transaction failed")
                    }
                }
            assertThat(result.exceptionOrNull()).isInstanceOf(IllegalStateException::class.java)
            assertThat(movieDao.countMovies()).isEqualTo(0)
        }
}
