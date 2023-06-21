package com.harshith.myapplication.data

import android.util.Log
import com.harshith.myapplication.data.source.local.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class DefaultTaskRepository @Inject constructor(
    //private val networkDataSource: NetworkDataSource,
    private val localDataSource: TaskDao
) : TaskRepository {
    override suspend fun createTask(title: String, description: String): String {
        val taskId = withContext(Dispatchers.IO){
            UUID.randomUUID().toString()
        }

        val task = Task(
            title = title,
            description = description,
            id = taskId
        )
        localDataSource.upsert(task.toLocal())
        /*
         * Won't be doing network part here
         */
        return taskId
    }

    override suspend fun updateTask(taskId: String, title: String, description: String) {
        val task = getTask(taskId)?.copy(
            title = title,
            description = description
        ) ?: throw Exception("Task (id $taskId) not found")
        localDataSource.upsert(task.toLocal())
    }

    override suspend fun completeTask(taskId: String) {
        localDataSource.updateCompleted(taskId = taskId, isCompleted = true)
    }

    override suspend fun activateTask(taskId: String) {
        localDataSource.updateCompleted(taskId = taskId, isCompleted = false)
    }

    override suspend fun clearCompletedTask() {
        localDataSource.deleteCompleted()
    }

    override suspend fun deleteTask(taskId: String) {
        localDataSource.deleteTaskById(taskId = taskId)
    }

    override suspend fun deleteAllTasks() {
        localDataSource.deleteAllTasks()
    }

    override fun getTasksStream(): Flow<List<Task>> {
        return localDataSource.observeAll().map { tasks->
            withContext(Dispatchers.IO){
                tasks.toExternal()
            }
        }
    }

    override suspend fun getTasks(forceUpdate: Boolean): List<Task> = withContext(Dispatchers.IO){
        localDataSource.getAll().toExternal()
    }

    override suspend fun refresh() {

    }

    override fun getTaskStream(taskId: String): Flow<Task?> {
        Log.e("ResponseStream", taskId)
        return localDataSource.observerById(taskId).map { it.toExternal() }
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Task? {
        return localDataSource.getTaskById(taskId = taskId).toExternal()
    }

    override suspend fun refreshTasks(taskId: String) {
        refresh()
    }


}