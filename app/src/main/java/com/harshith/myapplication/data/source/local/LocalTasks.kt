package com.harshith.myapplication.data.source.local

data class LocalTasks(
    val id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var isCompleted: Boolean = false
)