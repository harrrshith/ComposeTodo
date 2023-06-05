package com.harshith.myapplication.data.source.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TaskNetworkDataSource(): NetworkDataSource {
    private val mutex = Mutex()

    private var tasks = listOf(
        NetworkTask(
            "Task1",
            "Finish Task One",
            "Hey there!"

        ),
        NetworkTask(
            "Task2",
            "Finish Task Two",
            "Hey there! You again"

        )
    )
    override suspend fun loadTasks(): List<NetworkTask> = mutex.withLock{
        delay(2000L)
        return tasks
    }

    override suspend fun saveTasks(_tasks: List<NetworkTask>) = mutex.withLock{
        delay(2000L)
        tasks = _tasks
    }
}