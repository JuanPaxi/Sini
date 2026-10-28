package com.juanpaxi.sini.core.data.util

import com.juanpaxi.sini.core.network.SiniNetworkDataSource
import com.juanpaxi.sini.core.network.model.NetworkMovie
import com.juanpaxi.sini.core.network.model.NetworkMovieDetail
import com.juanpaxi.sini.core.network.retrofit.NetworkResponse

class TestSiniNetworkDataSource(
    private val source: SiniNetworkDataSource,
) : SiniNetworkDataSource {
    var exception: Exception? = null

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
    ): NetworkMovieDetail {
        exception?.let { throw it }
        return source.getMovieDetail(movieId, language)
    }
}
