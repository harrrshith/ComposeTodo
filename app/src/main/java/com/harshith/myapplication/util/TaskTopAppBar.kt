package com.harshith.myapplication.util

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.harshith.myapplication.R
import com.harshith.myapplication.ui.theme.ComposeTodoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTopAppBar (
    openDrawer: () -> Unit,
    onFilterAllTasks: () -> Unit,
    onFilerActiveTasks: () -> Unit,
    onFilterCompletedTasks: () -> Unit,
    onClearCompletedTasks: () -> Unit,
    onRefresh: () -> Unit
){
    TopAppBar(
        title = {Text(text = stringResource(id = R.string.app_name))},
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(Icons.Filled.Menu, stringResource(id = R.string.open_drawer) )
            }
        },
        actions = {
            FilterTasksMenu(onFilterAllTasks, onFilerActiveTasks, onFilterCompletedTasks)
            MoreTasksMenu(onClearCompletedTasks, onRefresh)
        }
    )
}

@Composable
fun FilterTasksMenu(
    onFilterAllTasks: () -> Unit,
    onFilterActiveTasks: () -> Unit,
    onFilterCompletedTasks: () -> Unit
){
    TopAppBarDropDownMenu(
        iconContent = {
            Icon(painter = painterResource(id = R.drawable.ic_filter), contentDescription = null)
        }
    ){closeMenu ->
        DropdownMenuItem(text = {stringResource(id = R.string.all)}, onClick = { onFilterAllTasks() ; closeMenu() })
        DropdownMenuItem(text = {stringResource(id = R.string.active)}, onClick = { onFilterActiveTasks() ;closeMenu() })
        DropdownMenuItem(text = {stringResource(id = R.string.completed)}, onClick = { onFilterCompletedTasks() ;closeMenu() })
    }
}

@Composable
fun TopAppBarDropDownMenu(
    iconContent: @Composable () -> Unit,
    content: @Composable ColumnScope.(() -> Unit) -> Unit
){
    var expanded by remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier
        .wrapContentSize(Alignment.TopEnd)){
        IconButton(onClick = {expanded = !expanded}) {
            iconContent()
        }
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false},
            modifier = Modifier.wrapContentSize(Alignment.TopCenter)
        ) {
            content{expanded = !expanded}
        }
    }
}

@Composable
fun MoreTasksMenu(
    onClearCompletedTasks: () -> Unit,
    onRefresh: () -> Unit
){
    TopAppBarDropDownMenu(
        iconContent = {
            Icon(Icons.Filled.MoreVert,
                contentDescription = null) }) {closeMenu ->
                    DropdownMenuItem(text = {stringResource(id = R.string.clear_completed)},
                        onClick = { onClearCompletedTasks(); closeMenu() })
                    DropdownMenuItem(text = { stringResource(id = R.string.refresh)},
                        onClick = { onRefresh() ; closeMenu() })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsTaskTopAppBar(
    openDrawer: () -> Unit
){
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.statistics))},
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(Icons.Filled.Menu, stringResource(id = R.string.statistics))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailTopAppBar(
    onBack: () -> Unit,
    onDelete: () -> Unit
){
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.task_details))},
        navigationIcon = {
           IconButton(onClick = onBack) {
               Icon(Icons.Filled.ArrowBack, stringResource(id = R.string.task_details))
           }
        },
        actions = {
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = stringResource(id = R.string.delete_tasks))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskTopAppBar(
    @StringRes title: Int,
    onBack: () -> Unit
){
    TopAppBar(
        title = { Text(text = stringResource(title))},
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(id = title))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun TaskTopAppBarPreview(){
    Surface {
        TaskTopAppBar(
            openDrawer = {},
            onFilterAllTasks = {},
            onFilerActiveTasks = {},
            onFilterCompletedTasks = {},
            onClearCompletedTasks = {}) {
            
        }
    }
}

@Preview
@Composable
fun StatisticsTaskTopAppBarPreview(){
    ComposeTodoTheme {
        Surface {
            StatisticsTaskTopAppBar(openDrawer = {})
        }
    }
}

@Preview
@Composable
fun TaskDetailTopAppBarPreview(){
    ComposeTodoTheme {
        Surface {
            TaskDetailTopAppBar(
                onBack = {},
                onDelete = {}
            )
        }
    }
}

@Preview
@Composable
fun AddEditTaskTopAppBarPreview(){
    ComposeTodoTheme {
        Surface{
            AddEditTaskTopAppBar(
                R.string.statistics,
                onBack = {}
            )
        }
    }
}

