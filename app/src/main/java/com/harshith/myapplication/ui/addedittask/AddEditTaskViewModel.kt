package com.harshith.myapplication.ui.addedittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshith.myapplication.R
import com.harshith.myapplication.TodoDestinationArgs
import com.harshith.myapplication.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditTaskUiState(
    val title: String = "",
    val description: String = "",
    val isTaskCompleted: Boolean = false,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isTaskSaved: Boolean = false
)
@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val taskId: String? = savedStateHandle[TodoDestinationArgs.TASK_ID_ARG]
    private val _uiState = MutableStateFlow(AddEditTaskUiState())
    val uiState: StateFlow<AddEditTaskUiState> = _uiState.asStateFlow()

    init {
        taskId?.let {
            loadTask(taskId = taskId)
        }
    }
    fun saveTask(){
        if(uiState.value.title.isEmpty() || uiState.value.description.isEmpty()){
            _uiState.update {
                it.copy(userMessage = R.string.empty_tasks)
            }
            return
        }
        else{
            if(taskId == null){
                createNewTask()
            }else{
                updateTask()
            }
        }
    }

    fun updateTitle(_title: String){
        _uiState.update {
            it.copy(title = _title)
        }
    }

    fun updateDescription(_description: String){
        _uiState.update {
            it.copy(description = _description)
        }
    }

    fun snackBarShown(){
        _uiState.update {
            it.copy(userMessage = null)
        }
    }

    private fun loadTask(taskId: String){
        viewModelScope.launch {
            taskRepository.getTask(taskId = taskId).let {task ->
                if(task != null){
                    _uiState.update {
                        it.copy(
                            title = task.title!!,
                            description = task.description!!,
                            isTaskCompleted = task.isCompleted,
                            isLoading = false
                        )
                    }
                }else{
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }

    private fun createNewTask() = viewModelScope.launch {
        taskRepository.createTask(uiState.value.title, uiState.value.description)
        _uiState.update {
            it.copy(isTaskSaved = true)
        }
    }

    private fun updateTask(){
        if(taskId == null)
            throw Exception("UpdateTask() was called instead of createNewTask()")
        else{
            viewModelScope.launch {
                taskRepository.updateTask(
                    taskId =  taskId,
                    title = uiState.value.title,
                    description = uiState.value.description
                )
                _uiState.update {
                    it.copy(isTaskSaved = true)
                }
            }
        }
    }
}