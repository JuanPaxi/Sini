package com.juanpaxi.sini.core.data.testdoubles

import com.juanpaxi.sini.core.network.SiniNetworkDataSource
import com.juanpaxi.sini.core.network.demo.DemoSiniNetworkDataSource
import com.juanpaxi.sini.core.network.model.NetworkMovie
import com.juanpaxi.sini.core.network.model.NetworkMovieDetail
import com.juanpaxi.sini.core.network.retrofit.NetworkResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.serialization.json.Json

class TestSiniNetworkDataSource : SiniNetworkDataSource {
    var exception: Exception? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    private val source =
        DemoSiniNetworkDataSource(
            ioDispatcher = UnconfinedTestDispatcher(),
            networkJson = Json { ignoreUnknownKeys = true },
        )

    override suspend fun getPopularMovies(
        page: Int,
        language: String,
    ): NetworkResponse<List<NetworkMovie>> {
        exception?.let { throw it }
        return source.getPopularMovies(page, language)
    }

    override suspend fun getMovieDetail(
        movieId: Int,
        language: String,
    ): NetworkMovieDetail = source.getMovieDetail(movieId, language)
}
