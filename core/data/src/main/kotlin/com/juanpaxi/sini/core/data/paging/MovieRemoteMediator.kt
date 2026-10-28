package com.juanpaxi.sini.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.juanpaxi.sini.core.data.Synchronizer
import com.juanpaxi.sini.core.database.dao.RemoteKeyDao
import com.juanpaxi.sini.core.database.model.MovieEntity
import com.juanpaxi.sini.core.database.model.RemoteKeyEntity
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator
    @Inject
    constructor(
        private val remoteKeyDao: RemoteKeyDao,
        private val synchronizer: Synchronizer,
    ) : RemoteMediator<Int, MovieEntity>() {
        override suspend fun load(
            loadType: LoadType,
            state: PagingState<Int, MovieEntity>,
        ): MediatorResult {
            return try {
                val remoteKey = remoteKeyDao.remoteKeyByCollection(COLLECTION)
                val page =
                    when (loadType) {
                        LoadType.REFRESH -> 1
                        LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                        LoadType.APPEND -> remoteKey?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                    }
                val totalPages = synchronizer.sync(page)
                when (loadType) {
                    LoadType.REFRESH -> {
                        remoteKeyDao.upsert(
                            remoteKey?.copy(
                                lastUpdated = Clock.System.now(),
                            ) ?: RemoteKeyEntity(COLLECTION, 2, Clock.System.now()),
                        )
                    }

                    LoadType.APPEND -> {
                        remoteKeyDao.upsert(
                            remoteKey!!.copy(
                                nextKey =
                                    if (page < totalPages && page < MAX_PAGE) {
                                        page + 1
                                    } else {
                                        null
                                    },
                            ),
                        )
                    }

                    LoadType.PREPEND -> {}
                }

                MediatorResult.Success(
                    endOfPaginationReached = page >= totalPages || page >= MAX_PAGE,
                )
            } catch (e: Exception) {
                MediatorResult.Error(e)
            }
        }

        override suspend fun initialize(): InitializeAction {
            val remoteKey =
                remoteKeyDao.remoteKeyByCollection(COLLECTION)
                    ?: return InitializeAction.LAUNCH_INITIAL_REFRESH
            val cacheAge = Clock.System.now() - remoteKey.lastUpdated
            return if (cacheAge >= CACHE_TIMEOUT) {
                InitializeAction.LAUNCH_INITIAL_REFRESH
            } else {
                InitializeAction.SKIP_INITIAL_REFRESH
            }
        }

        companion object {
            private const val COLLECTION = "popular"
            private val CACHE_TIMEOUT = 1.hours
            private const val MAX_PAGE = 500
        }
    }
