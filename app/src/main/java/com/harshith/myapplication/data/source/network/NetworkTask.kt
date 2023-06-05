package com.harshith.myapplication.data.source.network

data class NetworkTask(
    val id: String? = null,
    var title: String? = null,
    var shortDescription: String? = null,
    var priority: Int? = null,
    var status: TaskStatus = TaskStatus.ACTIVE
)
enum class TaskStatus{
    ACTIVE,
    COMPLETE
}