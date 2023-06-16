package com.harshith.myapplication.data.source.local

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM TASK")
    fun observeAll(): Flow<List<LocalTasks>>
}