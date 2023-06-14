package com.harshith.myapplication.util

sealed class Async<out T>{
    object Loading: Async<Nothing>() // As this does not return anything object is used.
    data class Error(val errorMessage: Int): Async<Nothing>()
    data class Success<out T>(val data: T): Async<T>()
}
