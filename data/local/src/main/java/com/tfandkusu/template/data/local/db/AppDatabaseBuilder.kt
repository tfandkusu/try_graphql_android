package com.tfandkusu.template.data.local.db

import android.content.Context
import androidx.room.Room

object AppDatabaseBuilder {
    fun build(applicationContext: Context, inMemory: Boolean): AppDatabase {
        return if (inMemory) {
            Room.inMemoryDatabaseBuilder(
                applicationContext,
                AppDatabase::class.java
            ).build()
        } else {
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database"
            ).build()
        }
    }
}
