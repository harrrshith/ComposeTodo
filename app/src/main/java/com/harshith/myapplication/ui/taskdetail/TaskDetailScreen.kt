package com.harshith.myapplication.ui.taskdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harshith.myapplication.R
import com.harshith.myapplication.data.Task
import com.harshith.myapplication.ui.theme.ComposeTodoTheme
import com.harshith.myapplication.util.TaskDetailTopAppBar

@Composable
fun TaskDetailScreen(
    onEditTask: (String) -> Unit,
    onBack: () -> Unit,
    onDeleteTask: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskDetailViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
){
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TaskDetailTopAppBar(
                onBack = onBack,
                onDelete = viewModel::onDeleteTask
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEditTask(viewModel.taskId) }
            ) {
                Icon(Icons.Filled.Edit, null )
            }
        }

    ) {paddingValues ->

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        EditTaskContent(
            loading = uiState.isLoading,
            empty = uiState.task == null && !uiState.isLoading,
            task = uiState.task,
            onTaskCheck = viewModel::setCompleted,
            onRefresh = {},
            modifier = modifier.padding(paddingValues)
        )

        uiState.userMessage?.let { userMessage ->
            val snackBarText = stringResource(id = userMessage)
            LaunchedEffect(viewModel, scaffoldState, userMessage, snackBarText){
                scaffoldState.snackbarHostState.showSnackbar(snackBarText)
                viewModel.snackBarMessageShown()
            }
        }

        LaunchedEffect(uiState.isTaskDeleted){
           if(uiState.isTaskDeleted){
               onDeleteTask()
           }
        }

    }

}

@Composable
fun EditTaskContent(
    loading: Boolean,
    empty: Boolean,
    task: Task?,
    onTaskCheck: (Boolean) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
){
    val screenPadding = Modifier.padding(
        horizontal = 16.dp,
        vertical = 8.dp
    )
    val commonModifier = modifier
        .fillMaxSize()
        .then(screenPadding)

    Surface {
        if(empty){
            Text(
                modifier = commonModifier,
                text = stringResource(id = R.string.no_data),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )
        }else{
            Column(commonModifier.verticalScroll(rememberScrollState())) {
                Row(
                    modifier
                        .fillMaxWidth()
                        .then(screenPadding)
                ) {
                    if(task != null){
                        Checkbox(
                            checked = task.isCompleted,
                            onCheckedChange = onTaskCheck
                        )
                        Column {
                            Text(
                                text = task.title!!,
                                style = MaterialTheme.typography.h6
                            )
                            Text(
                                text = task.description!!,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            }
        }
    }

}

@Preview
@Composable
fun EditTaskContentPreview(){
    ComposeTodoTheme {
        Surface {
            EditTaskContent(
                loading = false,
                empty = false,
                task = Task(
                    id = "111",
                    title = "Title",
                    description = "Description",
                    isCompleted = false
                ),
                onTaskCheck = {},
                onRefresh = {}
            )
        }
    }
}