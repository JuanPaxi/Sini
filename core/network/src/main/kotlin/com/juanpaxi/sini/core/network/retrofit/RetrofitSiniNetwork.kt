package com.juanpaxi.sini.core.network.retrofit

import com.juanpaxi.sini.core.network.SiniNetworkDataSource
import com.juanpaxi.sini.core.network.model.NetworkMovie
import com.juanpaxi.sini.core.network.model.NetworkMovieDetail
import dagger.Lazy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

interface RetrofitSiniNetworkApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("language") language: String,
    ): NetworkResponse<List<NetworkMovie>>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String,
    ): NetworkMovieDetail
}

private const val BASE_URL = "https://api.themoviedb.org/3/"

@Serializable
data class NetworkResponse<T>(
    val page: Int,
    val results: T,
    @SerialName("total_pages")
    val totalPages: Int,
)

@Singleton
internal class RetrofitSiniNetwork
    @Inject
    constructor(
        networkJson: Json,
        okhttpCallFactory: Lazy<Call.Factory>,
    ) : SiniNetworkDataSource {
        private val networkApi =
            Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .callFactory { okhttpCallFactory.get().newCall(it) }
                .addConverterFactory(
                    networkJson.asConverterFactory("application/json".toMediaType()),
                ).build()
                .create(RetrofitSiniNetworkApi::class.java)

        override suspend fun getPopularMovies(
            page: Int,
            language: String,
        ): List<NetworkMovie> =
            networkApi
                .getPopularMovies(
                    page = page,
                    language = language,
                ).results

        override suspend fun getMovieDetail(
            movieId: Int,
            language: String,
        ): NetworkMovieDetail =
            networkApi
                .getMovieDetail(
                    movieId = movieId,
                    language = language,
                )
    }
