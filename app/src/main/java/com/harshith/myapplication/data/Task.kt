package com.harshith.myapplication.data

data class Task(
    val id: String,
    var title: String? = null,
    var description: String? = null,
    var isCompleted: Boolean = false
){
    val titleForList: String
        get() = (title ?: description) as String

    val isActive
        get() = !isCompleted

    val isEmpty
        get() = title!!.isEmpty() || description!!.isEmpty()
}
