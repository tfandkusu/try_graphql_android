package com.tfandkusu.graphql.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tfandkusu.graphql.data.local.dao.CreatedDao
import com.tfandkusu.graphql.data.local.entity.LocalCreated

@Database(entities = [LocalCreated::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun createdDao(): CreatedDao
}
