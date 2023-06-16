package com.harshith.myapplication.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LocalTasks::class],
    version = 1,
    exportSchema = false
)
abstract class TodoDataBase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}