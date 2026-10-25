package com.juanpaxi.sini.core.database.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import com.juanpaxi.sini.core.database.model.RemoteKeyEntity
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

@Dao
interface RemoteKeyDao {
    @Query(
        """
        SELECT * FROM remote_keys
        WHERE collection = :collection
    """,
    )
    suspend fun remoteKeyByCollection(collection: String): RemoteKeyEntity?

    @Upsert
    suspend fun upsert(remoteKey: RemoteKeyEntity)

    @Query(
        """
        DELETE FROM remote_keys
        WHERE collection = :collection
    """,
    )
    suspend fun deleteByCollection(collection: String)

    @Query("SELECT lastUpdated FROM remote_keys WHERE collection = :collection")
    fun observeLastUpdated(collection: String): Flow<Instant?>
}
