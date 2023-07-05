package com.harshith.myapplication.ui.tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshith.myapplication.ADD_EDIT_RESULT_OK
import com.harshith.myapplication.DELETE_RESULT_OK
import com.harshith.myapplication.EDIT_RESULT_OK
import com.harshith.myapplication.R
import com.harshith.myapplication.data.Task
import com.harshith.myapplication.data.TaskRepository
import com.harshith.myapplication.ui.tasks.TasksFilterTypes.ACTIVE_TASKS
import com.harshith.myapplication.ui.tasks.TasksFilterTypes.ALL_TASKS
import com.harshith.myapplication.ui.tasks.TasksFilterTypes.COMPLETED_TASKS
import com.harshith.myapplication.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TasksUiState(
    val items: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val filteringUiInfo: FilteringUiInfo = FilteringUiInfo(),
    val userMessage: Int? = null
)

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _savedFilterType = savedStateHandle.getStateFlow(TASKS_FILTER_SAVED_STATE_KEY, ALL_TASKS)

    private val _filterUiInfo = _savedFilterType.map { getFilterUiInfo(it) }.distinctUntilChanged()
    private val _userMessage : MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _filteredTasksAsync = combine(
        taskRepository.getTasksStream(), _savedFilterType
    ){ tasks, type ->
            filterTasks(tasks, type)
    }
            .map { Async.Success(it) }
            .catch<Async<List<Task>>>{ emit(Async.Error(R.string.loading_error))}

    val uiState: StateFlow<TasksUiState> = combine(
        _filterUiInfo, _isLoading, _userMessage, _filteredTasksAsync
    ){filterUiInfo, isLoading, userMessage, filteredTaskAsync ->
        when(filteredTaskAsync){
            Async.Loading -> {
                TasksUiState(isLoading = true)
            }
            is Async.Error -> {
                TasksUiState(userMessage = filteredTaskAsync.errorMessage)
            }
            is Async.Success -> {
                TasksUiState(
                    items = filteredTaskAsync.data,
                    isLoading = isLoading,
                    filteringUiInfo = filterUiInfo,
                    userMessage = userMessage
                )
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1000),
            initialValue = TasksUiState(isLoading = true)
        )

    fun setFiltering(requestType: TasksFilterTypes){
        savedStateHandle[TASKS_FILTER_SAVED_STATE_KEY] = requestType
    }

    fun clearCompletedTasks(){
        viewModelScope.launch {
            if(taskRepository.getTasks().isNotEmpty()){
                taskRepository.clearCompletedTask()
                showSnackBarMessage(R.string.cleared_completed_tasks)
            }
            refresh()
        }
    }

    fun completeTask(task: Task,  completed: Boolean) = viewModelScope.launch {
        if(completed){
            taskRepository.completeTask(task.id)
            showSnackBarMessage(R.string.marked_complete)
        }else{
            taskRepository.activateTask(task.id)
            showSnackBarMessage(R.string.marked_active)
        }
    }

    fun showEditResultMessage(result: Int){
        when(result){
            EDIT_RESULT_OK -> showSnackBarMessage(R.string.task_saved_successfully)
            ADD_EDIT_RESULT_OK -> showSnackBarMessage(R.string.task_added_successfully)
            DELETE_RESULT_OK -> showSnackBarMessage(R.string.task_deleted_successfully)
        }
    }

    fun snackBarMessageShown(){
        _userMessage.value = null
    }

    private fun filterTasks(tasks: List<Task>, filterType: TasksFilterTypes): List<Task>{
        val tasksToShow = ArrayList<Task>()
        for(task in tasks){
            when(filterType){
                ALL_TASKS -> { tasksToShow.add(task)}
                ACTIVE_TASKS -> { if(task.isActive){tasksToShow.add(task)} }
                COMPLETED_TASKS -> { if(task.isCompleted){tasksToShow.add(task)} }
            }
        }
        return tasksToShow
    }

    private fun showSnackBarMessage(message: Int){
        _userMessage.value = message
    }

    fun refresh(){
        _isLoading.value = true
        viewModelScope.launch {
            taskRepository.refresh()
            _isLoading.value = false
        }
    }

    private fun getFilterUiInfo(filterType: TasksFilterTypes): FilteringUiInfo{
        return when(filterType){
            ALL_TASKS -> {
                FilteringUiInfo(
                    R.string.label_all,
                    R.string.no_tasks,
                    R.drawable.ic_empty
                )
            }
            ACTIVE_TASKS -> {
                FilteringUiInfo(
                    R.string.label_active,
                    R.string.no_tasks_active,
                    R.drawable.ic_circle
                )
            }
            COMPLETED_TASKS -> {
                FilteringUiInfo(
                    R.string.label_completed,
                    R.string.no_tasks_completed,
                    R.drawable.ic_verified_user
                )
            }
        }
    }

}
const val TASKS_FILTER_SAVED_STATE_KEY = "TASKS_FILTER_SAVED_STATE_KEY"

data class FilteringUiInfo(
    val currentFilterLabel: Int = R.string.label_all,
    val noTasksLabel: Int = R.string.no_tasks,
    val noTasksIcon: Int = R.drawable.ic_empty
)