package com.tfandkusu.graphql.data.local

import com.tfandkusu.graphql.data.local.db.AppDatabase
import com.tfandkusu.graphql.data.local.entity.LocalCreated
import javax.inject.Inject

/**
 * Local data store to save time for cache.
 */
interface CreatedLocalDataStore {
    /**
     * Get save time from kind parameter
     */
    suspend fun get(kind: String): Long

    /**
     * Set save time
     */
    suspend fun set(kind: String, createdAt: Long)

    suspend fun delete(kind: String)
}

class CreatedLocalDataStoreImpl @Inject constructor(db: AppDatabase) : CreatedLocalDataStore {

    private val dao = db.createdDao()

    override suspend fun get(kind: String) = dao.get(kind)?.createdAt ?: 0L

    override suspend fun set(kind: String, createdAt: Long) {
        val localCreated = dao.get(kind) ?: LocalCreated(0L, kind, 0L)
        dao.insert(localCreated.copy(createdAt = createdAt))
    }

    override suspend fun delete(kind: String) {
        dao.delete(kind)
    }
}
