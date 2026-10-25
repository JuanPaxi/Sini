package com.juanpaxi.sini.core.database.dao

import com.google.common.truth.Truth.assertThat
import com.juanpaxi.sini.core.database.model.RemoteKeyEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

internal class RemoteKeyDaoTest : DatabaseTest() {
    @Test
    fun remoteKeyByCollection_returnsNullWhenCollectionDoesNotExist() =
        runTest {
            val result = remoteKeyDao.remoteKeyByCollection("popular")
            assertThat(result).isNull()
        }

    @Test
    fun upsert_insertsNewRemoteKey() =
        runTest {
            val remoteKey = RemoteKeyEntity("popular", 2, Clock.System.now())
            remoteKeyDao.upsert(remoteKey)
            val result = remoteKeyDao.remoteKeyByCollection(remoteKey.collection)
            assertThat(result).isEqualTo(remoteKey)
        }

    @Test
    fun upsert_updatesExistingRemoteKey() =
        runTest {
            val remoteKey = RemoteKeyEntity("popular", 2, Clock.System.now())
            remoteKeyDao.upsert(remoteKey)
            val updatedRemoteKey = remoteKey.copy(nextKey = 3)
            remoteKeyDao.upsert(updatedRemoteKey)
            val result = remoteKeyDao.remoteKeyByCollection(remoteKey.collection)
            assertThat(result).isEqualTo(updatedRemoteKey)
        }

    @Test
    fun upsert_insertsRemoteKeyWithNullNextKey() =
        runTest {
            val remoteKey = RemoteKeyEntity("popular", null, Clock.System.now())
            remoteKeyDao.upsert(remoteKey)
            val result = remoteKeyDao.remoteKeyByCollection(remoteKey.collection)
            assertThat(result).isEqualTo(remoteKey)
        }

    @Test
    fun deleteByCollection_deletesRemoteKey() =
        runTest {
            val remoteKey = RemoteKeyEntity("popular", 2, Clock.System.now())
            remoteKeyDao.upsert(remoteKey)
            remoteKeyDao.deleteByCollection(remoteKey.collection)
            val result = remoteKeyDao.remoteKeyByCollection(remoteKey.collection)
            assertThat(result).isNull()
        }

    @Test
    fun observeLastUpdated_emitsNullWhenCollectionDoesNotExist() =
        runTest {
            val result = remoteKeyDao.observeLastUpdated("popular").first()
            assertThat(result).isNull()
        }

    @Test
    fun observeLastUpdated_emitsExistingValue() =
        runTest {
            val remoteKey = RemoteKeyEntity("popular", 2, Clock.System.now())
            remoteKeyDao.upsert(remoteKey)
            val result = remoteKeyDao.observeLastUpdated("popular").first()
            assertThat(result).isEqualTo(remoteKey.lastUpdated)
        }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeLastUpdated_emitsUpdatedValue() =
        runTest {
            val remoteKey = RemoteKeyEntity("popular", 2, Clock.System.now())
            remoteKeyDao.upsert(remoteKey)
            val emissions = Channel<Instant?>(Channel.UNLIMITED)
            val job =
                backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                    remoteKeyDao
                        .observeLastUpdated(remoteKey.collection)
                        .collect { emissions.send(it) }
                }
            assertThat(emissions.receive()).isEqualTo(remoteKey.lastUpdated)
            val updatedRemoteKey =
                remoteKey.copy(
                    nextKey = 3,
                    lastUpdated = remoteKey.lastUpdated + 1.hours,
                )
            remoteKeyDao.upsert(updatedRemoteKey)
            assertThat(emissions.receive()).isEqualTo(updatedRemoteKey.lastUpdated)
            job.cancel()
        }
}
