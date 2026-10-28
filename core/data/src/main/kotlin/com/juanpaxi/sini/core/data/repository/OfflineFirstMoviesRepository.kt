package com.juanpaxi.sini.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.juanpaxi.sini.core.data.MoviesRepository
import com.juanpaxi.sini.core.data.Synchronizer
import com.juanpaxi.sini.core.data.model.asExternalModel
import com.juanpaxi.sini.core.data.paging.MovieRemoteMediator
import com.juanpaxi.sini.core.database.dao.MovieDao
import com.juanpaxi.sini.core.database.dao.RemoteKeyDao
import com.juanpaxi.sini.core.model.data.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.Instant

internal class OfflineFirstMoviesRepository
    @Inject
    constructor(
        private val movieDao: MovieDao,
        private val remoteKeyDao: RemoteKeyDao,
        private val synchronizer: Synchronizer,
    ) : MoviesRepository {
        @OptIn(ExperimentalPagingApi::class)
        override fun getPopularMovies(): Flow<PagingData<Movie>> =
            Pager(
                config =
                    PagingConfig(
                        pageSize = PAGE_SIZE,
                        initialLoadSize = INITIAL_LOAD_SIZE,
                        prefetchDistance = PREFETCH_DISTANCE,
                    ),
                remoteMediator =
                    MovieRemoteMediator(
                        remoteKeyDao,
                        synchronizer,
                    ),
                pagingSourceFactory = {
                    movieDao.pagingSource()
                },
            ).flow.map { pagingData ->
                pagingData.map { movieEntity ->
                    movieEntity.asExternalModel()
                }
            }

        override suspend fun countMovies(): Int = movieDao.countMovies()

        override fun observeLastUpdated(): Flow<Instant?> =
            remoteKeyDao
                .observeLastUpdated(COLLECTION)
                .distinctUntilChanged()

        companion object {
            private const val COLLECTION = "popular"
            private const val PAGE_SIZE = 20
            private const val INITIAL_LOAD_SIZE = 20
            private const val PREFETCH_DISTANCE = 20
        }
    }
