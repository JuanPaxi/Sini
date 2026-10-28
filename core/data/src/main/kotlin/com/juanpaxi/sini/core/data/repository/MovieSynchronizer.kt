package com.juanpaxi.sini.core.data.repository

import com.juanpaxi.sini.core.data.Synchronizer
import com.juanpaxi.sini.core.data.model.asEntity
import com.juanpaxi.sini.core.database.TransactionRunner
import com.juanpaxi.sini.core.database.dao.MovieDao
import com.juanpaxi.sini.core.network.SiniNetworkDataSource
import javax.inject.Inject

class MovieSynchronizer
    @Inject
    constructor(
        private val networkDataSource: SiniNetworkDataSource,
        private val transactionRunner: TransactionRunner,
        private val movieDao: MovieDao,
    ) : Synchronizer {
        override suspend fun sync(page: Int): Int {
            val response = networkDataSource.getPopularMovies(page, "en-US")
            val movies = response.results
            val startPosition = (page - 1) * PAGE_SIZE
            val endPosition = startPosition + PAGE_SIZE - 1
            val existingMovies = movieDao.moviesInPositionRange(startPosition, endPosition)

            when (existingMovies.size) {
                PAGE_SIZE -> {
                    movieDao.upsertAll(
                        movies.mapIndexed { index, movie ->
                            movie.asEntity(
                                localId = existingMovies[index].localId,
                                position = startPosition + index,
                            )
                        },
                    )
                }

                else -> {
                    transactionRunner.run {
                        movieDao.deleteMovies(
                            startPosition = startPosition,
                            endPosition = endPosition,
                        )
                        movieDao.upsertAll(
                            movies.mapIndexed { index, movie ->
                                movie.asEntity(
                                    position = startPosition + index,
                                )
                            },
                        )
                    }
                }
            }

            return response.totalPages
        }

        companion object {
            private const val PAGE_SIZE = 20
        }
    }
