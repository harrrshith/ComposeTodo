package com.harshith.myapplication.ui.tasks

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harshith.myapplication.R
import com.harshith.myapplication.data.Task
import com.harshith.myapplication.ui.tasks.TasksFilterTypes.ACTIVE_TASKS
import com.harshith.myapplication.ui.tasks.TasksFilterTypes.ALL_TASKS
import com.harshith.myapplication.ui.tasks.TasksFilterTypes.COMPLETED_TASKS
import com.harshith.myapplication.ui.theme.ComposeTodoTheme
import com.harshith.myapplication.util.TaskTopAppBar

@Composable
fun TaskScreen (
    @StringRes userMessage: Int,
    onAddTask: () -> Unit,
    onTaskClick: (Task) -> Unit,
    onUserMessageDisplayed: () -> Unit,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
){
   Scaffold(
       scaffoldState = scaffoldState,
       topBar = {
           TaskTopAppBar(
               openDrawer = openDrawer,
               onFilterAllTasks = { viewModel.setFiltering(ALL_TASKS) },
               onFilerActiveTasks = { viewModel.setFiltering(ACTIVE_TASKS) },
               onFilterCompletedTasks = { viewModel.setFiltering(COMPLETED_TASKS) },
               onClearCompletedTasks = { viewModel.clearCompletedTasks() },
               onRefresh = {}
           )
       },
       modifier = modifier.fillMaxSize(),
       floatingActionButton = {
           FloatingActionButton(onClick = onAddTask) {
               Icon(Icons.Filled.Add, contentDescription = null )
           }
       }
   ) {paddingValue ->
       val uiState by viewModel.uiState.collectAsStateWithLifecycle()
       TaskContent(
           loading = uiState.isLoading,
           tasks = uiState.items,
           currentFilteringLabel = uiState.filteringUiInfo.currentFilterLabel,
           noTasksLabel = uiState.filteringUiInfo.noTasksLabel,
           noTasksIcon = uiState.filteringUiInfo.noTasksIcon,
           onRefresh = viewModel::refresh,
           onTaskClick = onTaskClick,
           onTaskCheckedChange = viewModel::completeTask,
           modifier = Modifier.padding(paddingValue)
       )


       uiState.userMessage?.let {message->
           val snackBarText = stringResource(id = message)
           LaunchedEffect(scaffoldState, viewModel, message, snackBarText){
               scaffoldState.snackbarHostState.showSnackbar(snackBarText)
               viewModel.snackBarMessageShown()
           }
       }

       val currentOnUserMessageDisplayed by rememberUpdatedState(newValue = onUserMessageDisplayed)
       LaunchedEffect(userMessage){
           userMessage.let {
               viewModel.showEditResultMessage(userMessage)
               currentOnUserMessageDisplayed()
           }
       }
   }
}

@Composable
fun TaskContent(
    loading: Boolean,
    tasks: List<Task>,
    @StringRes currentFilteringLabel: Int,
    @StringRes noTasksLabel: Int,
    @DrawableRes noTasksIcon: Int,
    onRefresh: () -> Unit,
    onTaskClick: (Task) -> Unit,
    onTaskCheckedChange: (Task, Boolean) -> Unit,
    modifier: Modifier = Modifier){

    if(tasks.isNotEmpty()){
        Surface {
            Column(
                modifier
                    .fillMaxSize()
                    .padding(16.dp, 8.dp)
            ) {

                Text(
                    text = stringResource(id = currentFilteringLabel),
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal)
                )

                LazyColumn{
                    items(tasks){task ->
                        TaskItem(
                            task = task,
                            onCheckedChange = {onTaskCheckedChange(task, it)},
                            onTaskClick = onTaskClick
                        )
                    }
                }
            }
        }
    }else{
        TasksEmptyContent(
            noTasksLabel,
            noTasksIcon
        )
    }
    
}

@Composable
private fun TaskItem(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onTaskClick: (Task) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onTaskClick(task) }
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = task.titleForList,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(
                start = 16.dp
            ),
            textDecoration = if (task.isCompleted) {
                TextDecoration.LineThrough
            } else {
                null
            }
        )
    }
}

@Composable
fun TasksEmptyContent(
    @StringRes noTasksLabel: Int,
    @DrawableRes noTasksIcon: Int,
    modifier: Modifier = Modifier
){
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = noTasksIcon),
                contentDescription = "Empty Screen",
                modifier.size(96.dp),
            )
            Text(
                text = stringResource(id = noTasksLabel),
                modifier.padding(top = 16.dp))
        }
    }
}

@Preview
@Composable
fun TaskContentPreview(){
    ComposeTodoTheme {
        Surface {
            TaskContent(
                loading = false,
                tasks = emptyList(),
                R.string.label_active,
                R.string.no_tasks_active,
                R.drawable.ic_empty,
                onRefresh = {},
                onTaskClick = {},
                onTaskCheckedChange = {_ , _->}
            )
        }
    }
}

@Preview
@Composable
fun TaskItemPreview(){
    ComposeTodoTheme {
        Surface{
            TaskItem(
                task = Task("1", "Task1", "New Task", false),
                onCheckedChange = {},
                onTaskClick = {}
            )
        }
    }
}

@Preview
@Composable
fun TaskItemCompletedPreview(){
    ComposeTodoTheme {
        Surface{
            TaskItem(
                task = Task("1", "Task1", "New Task", true),
                onCheckedChange = {},
                onTaskClick = {}
            )
        }
    }
}

@Preview
@Composable
fun TaskEmptyContentPreview(){
    ComposeTodoTheme {
        Surface {
            TasksEmptyContent(
                R.string.no_tasks,
                R.drawable.ic_empty
            )
        }
    }
}