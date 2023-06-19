package com.harshith.myapplication.data

import com.harshith.myapplication.data.source.local.LocalTask

fun Task.toLocal() = LocalTask(
    id = id!!,
    title = title,
    description = description,
    isCompleted = isCompleted
)

fun List<Task>.toLocal() = map(Task::toLocal)

fun LocalTask.toExternal() = Task(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted
)

@JvmName("localToExternal")
fun List<LocalTask>.toExternal() = map(LocalTask::toExternal)