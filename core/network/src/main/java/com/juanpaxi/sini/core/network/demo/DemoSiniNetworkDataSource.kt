package com.juanpaxi.sini.core.network.demo

import com.juanpaxi.sini.core.common.network.Dispatcher
import com.juanpaxi.sini.core.common.network.SiniDispatchers.IO
import com.juanpaxi.sini.core.network.SiniNetworkDataSource
import com.juanpaxi.sini.core.network.model.NetworkMovie
import com.juanpaxi.sini.core.network.model.NetworkMovieDetail
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject

class DemoSiniNetworkDataSource
    @Inject
    constructor(
        @param:Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
        private val networkJson: Json,
        private val assets: DemoAssetManager,
    ) : SiniNetworkDataSource {
        override suspend fun getPopularMovies(
            page: Int,
            language: String,
        ): List<NetworkMovie> = getDataFromJsonFile(MOVIE_ASSET)

        override suspend fun getMovieDetail(
            movieId: Int,
            language: String,
        ): NetworkMovieDetail = getDataFromJsonFile(MOVIE_DETAIL_ASSET)

        @OptIn(ExperimentalSerializationApi::class)
        private suspend inline fun <reified T> getDataFromJsonFile(fileName: String): T =
            withContext(ioDispatcher) {
                assets.open(fileName).use { inputStream ->
                    networkJson.decodeFromStream(inputStream)
                }
            }

        companion object {
            private const val MOVIE_ASSET = "movie.json"
            private const val MOVIE_DETAIL_ASSET = "movieDetail.json"
        }
    }
