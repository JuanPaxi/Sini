package com.juanpaxi.sini.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator.InitializeAction
import androidx.paging.RemoteMediator.MediatorResult.Error
import androidx.paging.RemoteMediator.MediatorResult.Success
import com.google.common.truth.Truth.assertThat
import com.juanpaxi.sini.core.data.testdoubles.TestRemoteKeyDao
import com.juanpaxi.sini.core.data.testdoubles.TestSynchronizer
import com.juanpaxi.sini.core.database.model.MovieEntity
import com.juanpaxi.sini.core.database.model.RemoteKeyEntity
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediatorTest {
    private lateinit var remoteKeyDao: TestRemoteKeyDao
    private lateinit var synchronizer: TestSynchronizer
    private lateinit var subject: MovieRemoteMediator

    private val pagingState =
        PagingState<Int, MovieEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0,
        )

    @Before
    fun setup() {
        remoteKeyDao = TestRemoteKeyDao()
        synchronizer = TestSynchronizer()
        subject =
            MovieRemoteMediator(
                remoteKeyDao = remoteKeyDao,
                synchronizer = synchronizer,
            )
    }

    @Test
    fun refreshCreatesRemoteKeyWhenMissing() =
        runTest {
            val result = subject.load(LoadType.REFRESH, pagingState)
            val remoteKey = remoteKeyDao.remoteKeyByCollection("popular")
            assertThat(synchronizer.requestedPage).isEqualTo(1)
            assertThat(remoteKey?.nextKey).isEqualTo(2)
            assertThat(result).isInstanceOf(Success::class.java)
            assertThat((result as Success).endOfPaginationReached).isFalse()
        }

    @Test
    fun refreshPreservesNextKeyWhenRemoteKeyExists() =
        runTest {
            val lastUpdated = Clock.System.now() - 2.hours
            remoteKeyDao.upsert(
                RemoteKeyEntity(
                    collection = "popular",
                    nextKey = 6,
                    lastUpdated = lastUpdated,
                ),
            )
            val result = subject.load(LoadType.REFRESH, pagingState)
            val remoteKey = remoteKeyDao.remoteKeyByCollection("popular")
            assertThat(synchronizer.requestedPage).isEqualTo(1)
            assertThat(remoteKey?.nextKey).isEqualTo(6)
            assertThat(remoteKey?.lastUpdated).isGreaterThan(lastUpdated)
            assertThat(result).isInstanceOf(Success::class.java)
            assertThat((result as Success).endOfPaginationReached).isFalse()
        }

    @Test
    fun prependReturnsEndOfPagination() =
        runTest {
            val result = subject.load(LoadType.PREPEND, pagingState)
            assertThat(result).isInstanceOf(Success::class.java)
            assertThat((result as Success).endOfPaginationReached).isTrue()
            assertThat(synchronizer.requestedPage).isNull()
        }

    @Test
    fun appendReturnsEndOfPaginationWhenNextKeyIsNull() =
        runTest {
            remoteKeyDao.upsert(
                RemoteKeyEntity(
                    collection = "popular",
                    nextKey = null,
                    lastUpdated = Clock.System.now(),
                ),
            )
            val result = subject.load(LoadType.APPEND, pagingState)
            assertThat(result).isInstanceOf(Success::class.java)
            assertThat((result as Success).endOfPaginationReached).isTrue()
            assertThat(synchronizer.requestedPage).isNull()
        }

    @Test
    fun appendLoadsNextPageAndUpdatesRemoteKey() =
        runTest {
            remoteKeyDao.upsert(
                RemoteKeyEntity(
                    collection = "popular",
                    nextKey = 6,
                    lastUpdated = Clock.System.now(),
                ),
            )
            val result = subject.load(LoadType.APPEND, pagingState)
            val remoteKey = remoteKeyDao.remoteKeyByCollection("popular")
            assertThat(synchronizer.requestedPage).isEqualTo(6)
            assertThat(remoteKey?.nextKey).isEqualTo(7)
            assertThat(result).isInstanceOf(Success::class.java)
            assertThat((result as Success).endOfPaginationReached).isFalse()
        }

    @Test
    fun appendReturnsEndOfPaginationWhenLastPageIsLoaded() =
        runTest {
            remoteKeyDao.upsert(
                RemoteKeyEntity(
                    collection = "popular",
                    nextKey = 10,
                    lastUpdated = Clock.System.now(),
                ),
            )
            val result = subject.load(LoadType.APPEND, pagingState)
            val remoteKey = remoteKeyDao.remoteKeyByCollection("popular")
            assertThat(synchronizer.requestedPage).isEqualTo(10)
            assertThat(remoteKey).isNotNull()
            assertThat(remoteKey!!.nextKey).isNull()
            assertThat(result).isInstanceOf(Success::class.java)
            assertThat((result as Success).endOfPaginationReached).isTrue()
        }

    @Test
    fun appendStopsPaginationAtMaxPage() =
        runTest {
            synchronizer.totalPages = 1000
            remoteKeyDao.upsert(
                RemoteKeyEntity(
                    collection = "popular",
                    nextKey = 500,
                    lastUpdated = Clock.System.now(),
                ),
            )
            val result = subject.load(LoadType.APPEND, pagingState)
            val remoteKey = remoteKeyDao.remoteKeyByCollection("popular")
            assertThat(synchronizer.requestedPage).isEqualTo(500)
            assertThat(remoteKey).isNotNull()
            assertThat(remoteKey!!.nextKey).isNull()
            assertThat(result).isInstanceOf(Success::class.java)
            assertThat((result as Success).endOfPaginationReached).isTrue()
        }

    @Test
    fun refreshReturnsErrorWhenSynchronizationFails() =
        runTest {
            val exception = IOException("Network error")
            synchronizer.exception = exception
            val result = subject.load(LoadType.REFRESH, pagingState)
            val remoteKey = remoteKeyDao.remoteKeyByCollection("popular")
            assertThat(synchronizer.requestedPage).isEqualTo(1)
            assertThat(result).isInstanceOf(Error::class.java)
            assertThat((result as Error).throwable).isSameInstanceAs(exception)
            assertThat(remoteKey).isNull()
        }

    @Test
    fun initializeLaunchesRefreshWhenRemoteKeyIsMissing() =
        runTest {
            val result = subject.initialize()
            assertThat(result).isEqualTo(InitializeAction.LAUNCH_INITIAL_REFRESH)
        }

    @Test
    fun initializeSkipsRefreshWhenCacheIsFresh() =
        runTest {
            remoteKeyDao.upsert(
                RemoteKeyEntity(
                    collection = "popular",
                    nextKey = 2,
                    lastUpdated = Clock.System.now(),
                ),
            )
            val result = subject.initialize()
            assertThat(result).isEqualTo(InitializeAction.SKIP_INITIAL_REFRESH)
        }

    @Test
    fun initializeLaunchesRefreshWhenCacheIsExpired() =
        runTest {
            remoteKeyDao.upsert(
                RemoteKeyEntity(
                    collection = "popular",
                    nextKey = 2,
                    lastUpdated = Clock.System.now() - 2.hours,
                ),
            )
            val result = subject.initialize()
            assertThat(result).isEqualTo(InitializeAction.LAUNCH_INITIAL_REFRESH)
        }
}
