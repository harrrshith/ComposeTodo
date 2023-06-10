package com.harshith.myapplication.ui.tasks

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harshith.myapplication.R
import com.harshith.myapplication.data.Task
import com.harshith.myapplication.ui.theme.ComposeTodoTheme
import com.harshith.myapplication.util.TaskTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen (){
   Scaffold(
       topBar = {
           TaskTopAppBar(
               openDrawer = {},
               onFilterAllTasks = {},
               onFilerActiveTasks = {},
               onFilterCompletedTasks = {},
               onClearCompletedTasks = {},
               onRefresh = {}
           )
       },
       modifier = Modifier.fillMaxSize()
   ) {
        //TODO
   }
}

@Composable
fun TaskContent(
    modifier: Modifier = Modifier,
    tasks: List<Task> = listOf(
        Task("1", "task1", stringResource(R.string.nice_task), false),
        Task("2", "task2", stringResource(R.string.nice_task), true),
        Task("3", "task3", stringResource(R.string.nice_task), false)),
    onRefresh: () -> Unit,
    onTaskClick: (Task) -> Unit,
    onTaskCheckedChange: (Task, Boolean) -> Unit){
    Surface {
        Column(
            modifier
                .fillMaxSize()
                .padding(16.dp, 8.dp)) {

            Text(text = "All Task",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.titleSmall)

            LazyColumn{
                items(tasks){task ->
                    TaskItem(
                        task = task,
                        onCheckedChange = {onTaskCheckedChange(task, it)},
                        onTaskClick = onTaskClick)
                }
            }
        }
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
            .clickable { }
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
    modifier: Modifier = Modifier
){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_notes),
            contentDescription = "Empty Screen",
            modifier.size(96.dp),
        )
        Text(
            text = stringResource(id = R.string.so_empty),
            modifier.padding(top = 16.dp))
    }
}

@Preview
@Composable
fun TaskContentPreview(){
    ComposeTodoTheme {
        Surface {
            TaskContent()
        }
    }
}

@Preview
@Composable
fun TaskItemPreview(){
    ComposeTodoTheme {
        Surface{
            TaskItem(
                task = Task("1", "Task1", "New Task")
                , onCheckedChange = {}
                , onTaskClick = {})
        }
    }
}

@Preview
@Composable
fun TaskItemCompletedPreview(){
    ComposeTodoTheme {
        Surface{
            TaskItem(
                task = Task("1", "Task1", "New Task", true)
                , onCheckedChange = {}
                , onTaskClick = {})
        }
    }
}

@Preview
@Composable
fun TaskEmptyContentPreview(){
    ComposeTodoTheme {
        Surface {
            TasksEmptyContent()
        }
    }
}

@Preview
@Composable
fun TaskScreenPreview(){
    ComposeTodoTheme {
        Surface {
            TaskScreen()
        }
    }
}