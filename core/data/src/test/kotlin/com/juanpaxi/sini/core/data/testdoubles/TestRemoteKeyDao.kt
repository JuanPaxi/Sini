package com.juanpaxi.sini.core.data.testdoubles

import com.juanpaxi.sini.core.database.dao.RemoteKeyDao
import com.juanpaxi.sini.core.database.model.RemoteKeyEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlin.time.Instant

class TestRemoteKeyDao : RemoteKeyDao {
    private val remoteKeys =
        MutableStateFlow<Map<String, RemoteKeyEntity>>(emptyMap())

    override suspend fun remoteKeyByCollection(collection: String): RemoteKeyEntity? = remoteKeys.value[collection]

    override suspend fun upsert(remoteKey: RemoteKeyEntity) {
        remoteKeys.update { keys ->
            keys + (remoteKey.collection to remoteKey)
        }
    }

    override suspend fun deleteByCollection(collection: String) {
        remoteKeys.update { keys ->
            keys - collection
        }
    }

    override fun observeLastUpdated(collection: String): Flow<Instant?> =
        remoteKeys.map { keys ->
            keys[collection]?.lastUpdated
        }
}
