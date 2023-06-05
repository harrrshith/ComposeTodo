package com.harshith.myapplication.data

import com.harshith.myapplication.data.source.local.TaskDao
import com.harshith.myapplication.data.source.network.NetworkDataSource
import kotlinx.coroutines.flow.Flow

class DefaultTaskRepository(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: TaskDao
) : TaskRepository {
    override suspend fun createTask(title: String, description: String) {

    }

    override suspend fun updateTask(title: String, description: String) {

    }

    override suspend fun completeTask(taskId: String) {

    }

    override suspend fun activateTask(taskId: String) {

    }

    override suspend fun clearCompletedTask() {

    }

    override suspend fun deleteTask(taskId: String) {

    }

    override suspend fun deleteAllTasks() {

    }

    override fun getTasksStream(): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasks(forceUpdate: Boolean): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun refresh() {
        TODO("Not yet implemented")
    }

    override fun getTaskStream(taskId: String): Flow<Task?> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun refreshTasks(taskId: String) {
        TODO("Not yet implemented")
    }


}