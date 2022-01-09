package com.tfandkusu.template.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tfandkusu.template.data.local.entity.LocalGithubRepo
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubRepoDao {
    @Query("SELECT * FROM githubRepo ORDER BY updatedAt DESC")
    fun listAsFlow(): Flow<List<LocalGithubRepo>>

    @Insert
    suspend fun insert(localGithubRepo: LocalGithubRepo)

    @Query("DELETE FROM githubRepo")
    suspend fun clear()
}
