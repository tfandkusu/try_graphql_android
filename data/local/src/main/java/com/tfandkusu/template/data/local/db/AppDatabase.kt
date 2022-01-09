package com.tfandkusu.template.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tfandkusu.template.data.local.dao.CreatedDao
import com.tfandkusu.template.data.local.dao.GithubRepoDao
import com.tfandkusu.template.data.local.entity.LocalCreated
import com.tfandkusu.template.data.local.entity.LocalGithubRepo

@Database(entities = [LocalGithubRepo::class, LocalCreated::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun githubRepoDao(): GithubRepoDao

    abstract fun createdDao(): CreatedDao
}
