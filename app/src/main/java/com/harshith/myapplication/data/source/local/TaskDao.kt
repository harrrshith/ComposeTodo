package com.harshith.myapplication.data.source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun observeAll(): Flow<List<LocalTask>>
    @Query("SELECT * FROM task WHERE id = :taskId")
    fun observerById(taskId: String): Flow<LocalTask>

    @Query("SELECT * FROM task")
    suspend fun getAll(): List<LocalTask>

    @Query("SELECT * FROM task WHERE id = :taskId")
    suspend fun getTaskById(taskId: String): LocalTask

    @Upsert
    suspend fun upsert(task: LocalTask)

    @Upsert
    suspend fun upsertAll(tasks: List<LocalTask>)

    @Query("UPDATE task SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun updateCompleted(taskId: String, isCompleted: Boolean)

    @Query("DELETE FROM task WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: String): Int

    @Query("DELETE FROM task")
    suspend fun deleteAllTasks()

    @Query("DELETE FROM task WHERE isCompleted = 1")
    suspend fun deleteCompleted()
}