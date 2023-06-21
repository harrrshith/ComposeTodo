package com.harshith.myapplication.ui.taskdetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshith.myapplication.R
import com.harshith.myapplication.TodoDestinationArgs
import com.harshith.myapplication.data.Task
import com.harshith.myapplication.data.TaskRepository
import com.harshith.myapplication.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskDetailUiState(
    val task: Task? = null,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isTaskDeleted: Boolean = false
)
@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val taskId: String = savedStateHandle[TodoDestinationArgs.TASK_ID_ARG]!!
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _isTaskDeleted = MutableStateFlow(false)
    private val _tasksAsync = taskRepository.getTaskStream(taskId)
        .map { handleTask(it) }
        .catch { emit(Async.Error(R.string.loading_error)) }

    val uiState: StateFlow<TaskDetailUiState> = combine(
        _tasksAsync, _isLoading, _userMessage, _isTaskDeleted
    ){tasksAsync, isLoading, userMessage, isTaskDeleted ->
        Log.e("ResponseUi", "$tasksAsync $isLoading $userMessage $isTaskDeleted")
        when(tasksAsync){
            Async.Loading ->{
                TaskDetailUiState(isLoading = true)
            }
            is Async.Error -> {
                TaskDetailUiState(
                    userMessage = tasksAsync.errorMessage,
                    isTaskDeleted = isTaskDeleted
                )
            }
            is Async.Success -> {
                TaskDetailUiState(
                    task = tasksAsync.data,
                    isLoading = isLoading,
                    userMessage = userMessage,
                    isTaskDeleted = isTaskDeleted
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TaskDetailUiState(isLoading = false)
    )
    fun onDeleteTask() = viewModelScope.launch {
        taskRepository.deleteTask(taskId = taskId)
        _isTaskDeleted.value = true
    }

    fun setCompleted(completed: Boolean) = viewModelScope.launch {
        val task = uiState.value.task ?: return@launch
        if(completed){
            taskRepository.completeTask(taskId = task.id)
            showSnackBarMessage(R.string.task_marked_complete)
        }else{
            taskRepository.activateTask(taskId = task.id)
            showSnackBarMessage(R.string.task_activated)
        }
    }

    fun snackBarMessageShown(){
        _userMessage.value = null
    }

    private fun showSnackBarMessage(message: Int){
        _userMessage.value = message
    }

    private fun handleTask(task: Task?): Async<Task?>{
        if(task == null){
            return Async.Error(R.string.task_not_found)
        }
        return Async.Success(task)
    }
}