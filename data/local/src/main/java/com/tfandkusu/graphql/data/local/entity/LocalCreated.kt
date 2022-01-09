package com.tfandkusu.graphql.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "created")
data class LocalCreated(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(index = true) val kind: String,
    val createdAt: Long
) {
    companion object {
        const val KIND_GITHUB_REPO = "githubRepo"
    }
}
