package com.tfandkusu.graphql.data.local.dao

import androidx.room.*
import com.tfandkusu.graphql.data.local.entity.LocalCreated

@Dao
interface CreatedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(localCreated: LocalCreated)

    @Query("SELECT * FROM created WHERE kind=:kind LIMIT 1")
    suspend fun get(kind: String): LocalCreated?

    @Query("DELETE FROM created WHERE kind=:kind")
    suspend fun delete(kind: String)
}
