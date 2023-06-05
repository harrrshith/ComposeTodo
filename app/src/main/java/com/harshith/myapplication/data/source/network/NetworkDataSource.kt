package com.harshith.myapplication.data.source.network

interface NetworkDataSource {
    suspend fun loadTasks() : List<NetworkTask>
    suspend fun saveTasks(_tasks: List<NetworkTask>)
}