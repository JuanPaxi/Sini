package com.juanpaxi.sini.core.data.repository

import android.content.Context
import androidx.paging.LoadState
import androidx.paging.testing.ErrorRecovery
import androidx.paging.testing.asSnapshot
import androidx.room3.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.juanpaxi.sini.core.data.util.TestSiniNetworkDataSource
import com.juanpaxi.sini.core.database.RoomTransactionRunner
import com.juanpaxi.sini.core.database.SiniDatabase
import com.juanpaxi.sini.core.database.dao.MovieDao
import com.juanpaxi.sini.core.database.dao.RemoteKeyDao
import com.juanpaxi.sini.core.database.model.MovieEntity
import com.juanpaxi.sini.core.database.model.RemoteKeyEntity
import com.juanpaxi.sini.core.network.demo.DemoAssetManager
import com.juanpaxi.sini.core.network.demo.DemoSiniNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.time.Instant
import kotlin.uuid.Uuid

class OfflineFirstMoviesRepositoryTest {
    private lateinit var db: SiniDatabase
    private lateinit var movieDao: MovieDao
    private lateinit var remoteKeyDao: RemoteKeyDao
    private lateinit var networkDataSource: TestSiniNetworkDataSource
    private lateinit var subject: OfflineFirstMoviesRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db =
            Room
                .inMemoryDatabaseBuilder(
                    context,
                    SiniDatabase::class.java,
                ).build()
        movieDao = db.movieDao()
        remoteKeyDao = db.remoteKeyDao()

        val demoNetworkDataSource =
            DemoSiniNetworkDataSource(
                ioDispatcher = Dispatchers.IO,
                networkJson =
                    Json {
                        ignoreUnknownKeys = true
                    },
                assets = DemoAssetManager(context.assets::open),
            )
        networkDataSource =
            TestSiniNetworkDataSource(
                source = demoNetworkDataSource,
            )
        val transactionRunner = RoomTransactionRunner(db)

        val synchronizer =
            MovieSynchronizer(
                networkDataSource = networkDataSource,
                transactionRunner = transactionRunner,
                movieDao = movieDao,
            )

        subject =
            OfflineFirstMoviesRepository(
                movieDao = movieDao,
                remoteKeyDao = remoteKeyDao,
                synchronizer = synchronizer,
            )
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun getPopularMoviesLoadsMoviesWhenDatabaseIsEmpty() =
        runTest {
            val movies = subject.getPopularMovies().asSnapshot()
            val storedMovies = movieDao.countMovies()
            val remoteKey = remoteKeyDao.remoteKeyByCollection("popular")
            assertThat(movies).isNotEmpty()
            assertThat(storedMovies).isAtLeast(20)
            assertThat(remoteKey).isNotNull()
            val hope = movies.first()
            assertThat(hope.id).isEqualTo(1058424)
            assertThat(hope.title).isEqualTo("Hope")
        }

    @Test
    fun getPopularMoviesReportsRefreshErrorWhenNetworkIsUnavailable() =
        runTest {
            val exception = IOException("No internet")
            networkDataSource.exception = exception
            var refreshError: LoadState.Error? = null
            subject.getPopularMovies().asSnapshot(
                onError = { states ->
                    refreshError = states.mediator?.refresh as? LoadState.Error
                    ErrorRecovery.RETURN_CURRENT_SNAPSHOT
                },
            )

            assertThat(refreshError).isNotNull()
            assertThat(refreshError?.error).isSameInstanceAs(exception)
            assertThat(movieDao.countMovies()).isEqualTo(0)
        }

    @Test
    fun getPopularMoviesLoadsMoreMoviesWhenScrolling() =
        runTest {
            val movies =
                subject.getPopularMovies().asSnapshot {
                    scrollTo(index = 39)
                }
            val remoteKey = remoteKeyDao.remoteKeyByCollection("popular")
            assertThat(movies.size).isGreaterThan(20)
            assertThat(remoteKey).isNotNull()
            assertThat(remoteKey?.nextKey ?: 0).isGreaterThan(2)
        }

    @Test
    fun countMoviesReturnsNumberOfStoredMovies() =
        runTest {
            val movies =
                listOf(
                    MovieEntity(
                        localId = Uuid.random(),
                        tmdbId = 101,
                        position = 0,
                        title = "Movie 1",
                        posterPath = null,
                        voteAverage = 7.5,
                    ),
                    MovieEntity(
                        localId = Uuid.random(),
                        tmdbId = 102,
                        position = 1,
                        title = "Movie 2",
                        posterPath = null,
                        voteAverage = 8.0,
                    ),
                )
            movieDao.upsertAll(movies)
            assertThat(subject.countMovies()).isEqualTo(2)
        }

    @Test
    fun observeLastUpdatedEmitsNullWhenNoRemoteKeyExists() =
        runTest {
            val lastUpdated = subject.observeLastUpdated().first()
            assertThat(lastUpdated).isNull()
        }

    @Test
    fun observeLastUpdatedEmitsStoredLastUpdated() =
        runTest {
            val expectedLastUpdated = Instant.fromEpochMilliseconds(1_000)
            remoteKeyDao.upsert(
                RemoteKeyEntity(
                    collection = "popular",
                    nextKey = 2,
                    lastUpdated = expectedLastUpdated,
                ),
            )
            val lastUpdated = subject.observeLastUpdated().first()
            assertThat(lastUpdated).isEqualTo(expectedLastUpdated)
        }
}
