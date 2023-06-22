package com.harshith.myapplication.ui.satistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshith.myapplication.R
import com.harshith.myapplication.data.Task
import com.harshith.myapplication.data.TaskRepository
import com.harshith.myapplication.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class StatisticsUiState(
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val activeTasksPercent: Float = 0f,
    val completedTaskPercent: Float = 0f
)
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    val uiState: StateFlow<StatisticsUiState> = taskRepository.getTasksStream()
        .map { Async.Success(it) }
        .catch<Async<List<Task>>> { emit(Async.Error(R.string.loading_error)) }
        .map { taskAsync -> produceStatisticsUiState(taskAsync) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = StatisticsUiState(isLoading = true)
        )


    private fun produceStatisticsUiState(tasks: Async<List<Task>>): StatisticsUiState{
        return when(tasks){
            Async.Loading -> {
                StatisticsUiState(
                    isLoading = true,
                    isEmpty = true
                )
            }
            is Async.Error -> {
                StatisticsUiState(
                    isEmpty = true,
                    isLoading = false
                )
            }
            is Async.Success -> {
                val stats = getActiveAndCompletedStatus(tasks.data)
                StatisticsUiState(
                    isEmpty = tasks.data.isEmpty(),
                    isLoading = false,
                    activeTasksPercent = stats.activeTasksPercentage,
                    completedTaskPercent = stats.completedTasksPercentage
                )
            }
        }
    }
}
private fun getActiveAndCompletedStatus(tasks: List<Task>): StatusResult {
    return if(tasks.isEmpty()){
        StatusResult(0f, 0f)
    }else{
        val totalTask = tasks.size
        val totalActive = tasks.count{ it.isActive}
        StatusResult(
            activeTasksPercentage = 100f * totalActive / totalTask,
            completedTasksPercentage = 100f * (totalTask - totalActive) / totalTask
        )
    }

}

data class StatusResult(
    val activeTasksPercentage: Float,
    val completedTasksPercentage: Float
)
