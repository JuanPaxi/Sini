package com.juanpaxi.sini.core.network

import com.juanpaxi.sini.core.network.model.NetworkMovie
import com.juanpaxi.sini.core.network.model.NetworkMovieDetail
import com.juanpaxi.sini.core.network.retrofit.NetworkResponse

interface SiniNetworkDataSource {
    suspend fun getPopularMovies(
        page: Int,
        language: String,
    ): NetworkResponse<List<NetworkMovie>>

    suspend fun getMovieDetail(
        movieId: Int,
        language: String,
    ): NetworkMovieDetail
}
