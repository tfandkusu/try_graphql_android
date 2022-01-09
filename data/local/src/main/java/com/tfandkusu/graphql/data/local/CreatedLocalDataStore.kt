package com.tfandkusu.graphql.data.local

import com.tfandkusu.graphql.data.local.db.AppDatabase
import javax.inject.Inject

/**
 * Local data store to save time for cache.
 */
interface CreatedLocalDataStore {
    /**
     * Get save time from kind parameter
     */
    suspend fun get(kind: String): Long
}

class CreatedLocalDataStoreImpl @Inject constructor(db: AppDatabase) : CreatedLocalDataStore {

    private val dao = db.createdDao()

    override suspend fun get(kind: String) = dao.get(kind)?.createdAt ?: 0L
}
