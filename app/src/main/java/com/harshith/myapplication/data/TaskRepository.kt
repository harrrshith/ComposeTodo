package com.harshith.myapplication.data

import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasksStream() : Flow<List<Task>> // return the whole list of tasks present
    suspend fun getTasks(forceUpdate: Boolean = false): List<Task>
    suspend fun refresh()
    fun getTaskStream(taskId: String): Flow<Task?>
    suspend fun getTask(taskId: String, forceUpdate: Boolean = false): Task?
    suspend fun refreshTasks(taskId: String)
    suspend fun createTask(title: String, description: String): String
    suspend fun updateTask(taskId: String, title: String, description: String)
    suspend fun completeTask(taskId: String)
    suspend fun activateTask(taskId: String)
    suspend fun clearCompletedTask()
    suspend fun deleteTask(taskId: String)
    suspend fun deleteAllTasks()

}