package com.harshith.myapplication.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "task"
)
data class LocalTasks(
    @PrimaryKey val id: String,
    var title: String? = null,
    var description: String? = null,
    var isCompleted: Boolean = false
)