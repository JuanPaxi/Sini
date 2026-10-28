package com.juanpaxi.sini.core.data.repository

import com.juanpaxi.sini.core.data.MovieDetailRepository
import com.juanpaxi.sini.core.data.model.asEntity
import com.juanpaxi.sini.core.data.model.asExternalModel
import com.juanpaxi.sini.core.database.dao.MovieDetailDao
import com.juanpaxi.sini.core.model.data.MovieDetail
import com.juanpaxi.sini.core.network.SiniNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

internal class OfflineFirstMovieDetailRepository
    @Inject
    constructor(
        private val networkDataSource: SiniNetworkDataSource,
        private val movieDetailDao: MovieDetailDao,
    ) : MovieDetailRepository {
        override suspend fun getMovieDetail(movieId: Int): MovieDetail {
            val networkMovieDetail =
                try {
                    networkDataSource.getMovieDetail(movieId, language = "en-US")
                } catch (e: IOException) {
                    return movieDetailDao.movieDetail(movieId)?.asExternalModel() ?: throw e
                }
            val existingMovie = movieDetailDao.movieDetail(movieId)
            val entity =
                networkMovieDetail.asEntity(
                    isFavorite = existingMovie?.isFavorite ?: false,
                )
            movieDetailDao.upsert(entity)
            return movieDetailDao.movieDetail(movieId)!!.asExternalModel()
        }

        override suspend fun setFavorite(
            movieId: Int,
            isFavorite: Boolean,
        ) = movieDetailDao.updateFavorite(movieId, isFavorite)

        override fun observeFavorites(): Flow<List<MovieDetail>> =
            movieDetailDao
                .favorites()
                .map { entities ->
                    entities.map { it.asExternalModel() }
                }
    }
