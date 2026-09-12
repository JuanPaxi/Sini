package com.juanpaxi.sini.core.network.demo

import JvmUnitTestDemoAssetManager
import com.juanpaxi.sini.core.network.model.NetworkGenre
import com.juanpaxi.sini.core.network.model.NetworkMovie
import com.juanpaxi.sini.core.network.model.NetworkMovieDetail
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DemoSiniNetworkDataSourceTest {
    private lateinit var subject: DemoSiniNetworkDataSource

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        subject =
            DemoSiniNetworkDataSource(
                ioDispatcher = testDispatcher,
                networkJson = Json { ignoreUnknownKeys = true },
                assets = JvmUnitTestDemoAssetManager,
            )
    }

    @Suppress("ktlint:standard:max-line-length")
    @Test
    fun getPopularMovies_returnsDeserializedMovies() =
        runTest(testDispatcher) {
            assertEquals(
                NetworkMovie(
                    id = 969681,
                    title = "Spider-Man: Brand New Day",
                    posterPath = "/bjiS5ipwxb9JFy3XRRN4OAilSeX.jpg",
                    voteAverage = 7.855,
                    releaseDate = "2026-07-29",
                ),
                subject.getPopularMovies(page = 1, language = "en-US").first(),
            )
        }

    @Suppress("ktlint:standard:max-line-length")
    @Test
    fun getMovieDetail_returnsDeserializedMovie() =
        runTest(testDispatcher) {
            assertEquals(
                NetworkMovieDetail(
                    id = 1204680,
                    title = "Coyote vs. Acme",
                    posterPath = "/vhv7lBWYM0DUuNU2a0V7Rhq21dD.jpg",
                    backdropPath = "/l9mFW9HQnAZ4r1ChZJHoOT3jaal.jpg",
                    overview = "After Acme products fail him one too many times in his dogged pursuit of the Roadrunner, Wile E. Coyote decides to hire a billboard lawyer to sue the Acme Corporation.",
                    voteAverage = 7.575,
                    releaseDate = "2026-08-20",
                    runtime = 103,
                    genres =
                        listOf(
                            NetworkGenre(
                                id = 35,
                                name = "Comedy",
                            ),
                            NetworkGenre(
                                id = 12,
                                name = "Adventure",
                            ),
                            NetworkGenre(
                                id = 10751,
                                name = "Family",
                            ),
                        ),
                ),
                subject.getMovieDetail(movieId = 1204680, language = "en-US"),
            )
        }
}
