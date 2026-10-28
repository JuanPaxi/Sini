package com.juanpaxi.sini.core.data.repository

import android.content.Context
import androidx.room3.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.juanpaxi.sini.core.data.util.TestSiniNetworkDataSource
import com.juanpaxi.sini.core.database.SiniDatabase
import com.juanpaxi.sini.core.database.dao.MovieDetailDao
import com.juanpaxi.sini.core.database.model.MovieDetailEntity
import com.juanpaxi.sini.core.model.data.MovieDetail
import com.juanpaxi.sini.core.network.demo.DemoAssetManager
import com.juanpaxi.sini.core.network.demo.DemoSiniNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class OfflineFirstMovieDetailRepositoryTest {
    private lateinit var db: SiniDatabase
    private lateinit var movieDetailDao: MovieDetailDao
    private lateinit var networkDataSource: TestSiniNetworkDataSource
    private lateinit var subject: OfflineFirstMovieDetailRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db =
            Room
                .inMemoryDatabaseBuilder(
                    context,
                    SiniDatabase::class.java,
                ).build()

        movieDetailDao = db.movieDetailDao()
        val demoNetworkDataSource =
            DemoSiniNetworkDataSource(
                ioDispatcher = Dispatchers.IO,
                networkJson = Json { ignoreUnknownKeys = true },
                assets = DemoAssetManager(context.assets::open),
            )
        networkDataSource =
            TestSiniNetworkDataSource(
                source = demoNetworkDataSource,
            )
        subject =
            OfflineFirstMovieDetailRepository(
                networkDataSource = networkDataSource,
                movieDetailDao = movieDetailDao,
            )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getMovieDetailLoadsMovieAsNotFavoriteWhenNetworkSucceedsAndDatabaseIsEmpty() =
        runTest {
            val movieId = 969681
            val result = subject.getMovieDetail(movieId)
            val savedMovie = movieDetailDao.movieDetail(tmdbId = movieId)
            assertThat(savedMovie).isNotNull()
            assertThat(savedMovie!!.isFavorite).isFalse()
            assertThat(result.id).isEqualTo(movieId)
        }

    @Test
    fun getMovieDetailUpdatesCachedMovieAndPreservesFavoriteWhenNetworkSucceeds() =
        runTest {
            val movieId = 969681
            val cachedMovie =
                MovieDetailEntity(
                    tmdbId = movieId,
                    title = "Old title",
                    posterPath = null,
                    backdropPath = null,
                    overview = "Old overview",
                    voteAverage = 5.0,
                    releaseDate = null,
                    runtime = null,
                    genres = emptyList(),
                    isFavorite = true,
                )
            movieDetailDao.upsert(cachedMovie)
            val result = subject.getMovieDetail(movieId)
            val savedMovie = movieDetailDao.movieDetail(movieId)
            assertThat(savedMovie).isNotNull()
            assertThat(savedMovie!!.isFavorite).isTrue()
            assertThat(savedMovie.title).isNotEqualTo("Old title")
            assertThat(savedMovie.overview).isNotEqualTo("Old overview")
            assertThat(result.title).isEqualTo(savedMovie.title)
        }

    @Test
    fun getMovieDetailReturnsCachedMovieWhenNetworkFails() =
        runTest {
            val movieId = 969681
            val cachedMovie =
                MovieDetailEntity(
                    tmdbId = movieId,
                    title = "Cached movie",
                    posterPath = "/cached-poster.jpg",
                    backdropPath = null,
                    overview = "Cached overview",
                    voteAverage = 7.5,
                    releaseDate = null,
                    runtime = null,
                    genres = listOf("Drama"),
                    isFavorite = true,
                )
            movieDetailDao.upsert(cachedMovie)
            networkDataSource.exception = IOException("Network unavailable")
            val result = subject.getMovieDetail(movieId)
            assertThat(result.id).isEqualTo(movieId)
            assertThat(result.title).isEqualTo("Cached movie")
            assertThat(result.overview).isEqualTo("Cached overview")
            assertThat(result.isFavorite).isTrue()
        }

    @Test
    fun getMovieDetailRethrowsIOExceptionWhenNetworkFailsAndDatabaseIsEmpty() =
        runTest {
            val movieId = 969681
            val exception = IOException("Network unavailable")
            networkDataSource.exception = exception
            val thrown =
                try {
                    subject.getMovieDetail(movieId)
                    null
                } catch (e: IOException) {
                    e
                }
            assertThat(thrown).isSameInstanceAs(exception)
            assertThat(movieDetailDao.movieDetail(movieId)).isNull()
        }

    @Test
    fun getMovieDetailPreservesNonFavoriteWhenNetworkSucceeds() =
        runTest {
            val movieId = 969681
            val cachedMovie =
                MovieDetailEntity(
                    tmdbId = movieId,
                    title = "Old title",
                    posterPath = null,
                    backdropPath = null,
                    overview = "Old overview",
                    voteAverage = 5.0,
                    releaseDate = null,
                    runtime = null,
                    genres = emptyList(),
                    isFavorite = false,
                )
            movieDetailDao.upsert(cachedMovie)
            val result = subject.getMovieDetail(movieId)
            val savedMovie = movieDetailDao.movieDetail(movieId)
            assertThat(savedMovie).isNotNull()
            assertThat(savedMovie!!.isFavorite).isFalse()
            assertThat(result.isFavorite).isFalse()
            assertThat(savedMovie.title).isNotEqualTo("Old title")
        }

    @Test
    fun setFavoriteMarksMovieAsFavoriteWhenMovieIsNotFavorite() =
        runTest {
            val movieId = 969681
            val movie =
                MovieDetailEntity(
                    tmdbId = movieId,
                    title = "Test movie",
                    posterPath = null,
                    backdropPath = null,
                    overview = null,
                    voteAverage = 7.0,
                    releaseDate = null,
                    runtime = null,
                    genres = emptyList(),
                    isFavorite = false,
                )
            movieDetailDao.upsert(movie)
            subject.setFavorite(movieId, true)
            val updatedMovie = movieDetailDao.movieDetail(tmdbId = movieId)
            assertThat(updatedMovie).isNotNull()
            assertThat(updatedMovie!!.isFavorite).isTrue()
        }

    @Test
    fun setFavoriteMarksMovieAsNotFavoriteWhenMovieIsFavorite() =
        runTest {
            val movieId = 969681
            val movie =
                MovieDetailEntity(
                    tmdbId = movieId,
                    title = "Test movie",
                    posterPath = null,
                    backdropPath = null,
                    overview = null,
                    voteAverage = 7.0,
                    releaseDate = null,
                    runtime = null,
                    genres = emptyList(),
                    isFavorite = true,
                )
            movieDetailDao.upsert(movie)
            subject.setFavorite(movieId, false)
            val updatedMovie = movieDetailDao.movieDetail(movieId)
            assertThat(updatedMovie).isNotNull()
            assertThat(updatedMovie!!.isFavorite).isFalse()
        }

    @Test
    fun observeFavoritesEmitsOnlyFavoriteMoviesInTitleOrder() =
        runTest {
            movieDetailDao.upsert(
                MovieDetailEntity(
                    tmdbId = 1,
                    title = "Alpha",
                    posterPath = null,
                    backdropPath = null,
                    overview = "First movie",
                    voteAverage = 7.0,
                    releaseDate = null,
                    runtime = null,
                    genres = emptyList(),
                    isFavorite = true,
                ),
            )
            movieDetailDao.upsert(
                MovieDetailEntity(
                    tmdbId = 2,
                    title = "Beta",
                    posterPath = null,
                    backdropPath = null,
                    overview = "Second movie",
                    voteAverage = 6.0,
                    releaseDate = null,
                    runtime = null,
                    genres = emptyList(),
                    isFavorite = false,
                ),
            )
            movieDetailDao.upsert(
                MovieDetailEntity(
                    tmdbId = 3,
                    title = "Gamma",
                    posterPath = null,
                    backdropPath = null,
                    overview = "Third movie",
                    voteAverage = 8.0,
                    releaseDate = null,
                    runtime = null,
                    genres = emptyList(),
                    isFavorite = true,
                ),
            )
            val favorites = subject.observeFavorites().first()
            assertThat(favorites).hasSize(2)
            assertThat(favorites.map { it.id }).containsExactly(1, 3).inOrder()
            assertThat(favorites.map { it.title }).containsExactly("Alpha", "Gamma").inOrder()
            assertThat(favorites.map { it.overview }).containsExactly("First movie", "Third movie").inOrder()
        }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeFavoritesEmitsUpdatedListWhenFavoriteChanges() =
        runTest {
            val movie =
                MovieDetailEntity(
                    tmdbId = 1,
                    title = "Alpha",
                    posterPath = null,
                    backdropPath = null,
                    overview = null,
                    voteAverage = 7.0,
                    releaseDate = null,
                    runtime = null,
                    genres = emptyList(),
                    isFavorite = false,
                )
            movieDetailDao.upsert(movie)
            val emissions = Channel<List<MovieDetail>>(Channel.UNLIMITED)
            backgroundScope.launch(
                context = UnconfinedTestDispatcher(testScheduler),
            ) {
                subject.observeFavorites().collect { favorites ->
                    emissions.send(element = favorites)
                }
            }
            assertThat(emissions.receive()).isEmpty()
            subject.setFavorite(movieId = 1, isFavorite = true)
            val favoritesAfterAdding = emissions.receive()
            assertThat(favoritesAfterAdding).hasSize(1)
            assertThat(favoritesAfterAdding.map { it.id }).containsExactly(1)
            assertThat(favoritesAfterAdding.map { it.title }).containsExactly("Alpha")
            subject.setFavorite(movieId = 1, isFavorite = false)
            assertThat(emissions.receive()).isEmpty()
        }
}
