package com.harshith.myapplication

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.harshith.myapplication.TodoDestinationArgs.TASK_ID_ARG
import com.harshith.myapplication.TodoDestinationArgs.TITLE_ARG
import com.harshith.myapplication.TodoDestinationArgs.USER_MSG_ARG
import com.harshith.myapplication.ui.addedittask.AddEditTaskScreen
import com.harshith.myapplication.ui.satistics.StatisticsScreen
import com.harshith.myapplication.ui.taskdetail.TaskDetailScreen
import com.harshith.myapplication.ui.tasks.TaskScreen
import com.harshith.myapplication.util.AppModalDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TodoNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: androidx.compose.material.DrawerState = androidx.compose.material.rememberDrawerState(
        initialValue = androidx.compose.material.DrawerValue.Closed
    ),
    startDestination: String = TodoDestination.TASK_ROUTE,
    navActions: TodoNavigationAction = remember(navController) {
        TodoNavigationAction(navController)
    }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ){
        composable(
            TodoDestination.TASK_ROUTE,
            arguments = listOf(
                navArgument(USER_MSG_ARG){type = NavType.IntType; defaultValue = 0}
            )
        ){entry ->
            AppModalDrawer(
                drawerState = drawerState,
                currentRoute = currentRoute,
                navigationActions = navActions
            ) {
                TaskScreen(
                    userMessage = entry.arguments?.getInt(USER_MSG_ARG)!!,
                    onAddTask = { navActions.navigateToAddEditTask(R.string.add_task, null) },
                    onTaskClick = {task -> navActions.navigateToTaskDetails(task.id) },
                    onUserMessageDisplayed = { entry.arguments?.putInt(USER_MSG_ARG, 0) },
                    openDrawer = { coroutineScope.launch { drawerState.open() } }
                )
            }

        }
        composable(TodoDestination.STATISTICS_ROUTE){
            AppModalDrawer(
                drawerState = drawerState,
                currentRoute = currentRoute,
                navigationActions = navActions
            ) {
                StatisticsScreen(openDrawer = {coroutineScope.launch { drawerState.open() }})
            }
        }
        composable(
            TodoDestination.ADD_EDIT_TASK_ROUTE,
            arguments = listOf(
                navArgument(TITLE_ARG){ type = NavType.IntType},
                navArgument(TASK_ID_ARG){ type = NavType.StringType; nullable = true}
            )
        ){entry ->
            val taskId = entry.arguments?.getInt(TASK_ID_ARG)
            AddEditTaskScreen(
                topBarTitle = entry.arguments?.getInt(TITLE_ARG)!!,
                onTaskUpdate = {
                               navActions.navigateToTasks(
                                   if(taskId == null)
                                       ADD_EDIT_RESULT_OK
                                   else
                                     EDIT_RESULT_OK
                               )
                }, onBack = { navController.popBackStack() }
            )
        }
        composable(TodoDestination.TASK_DETAILS_ROUTE){
            TaskDetailScreen(
                onEditTask = { taskId ->
                    navActions.navigateToAddEditTask(R.string.edit_task, taskId)
                },
                onBack = { navController.popBackStack() },
                onDeleteTask = { navActions.navigateToTasks(DELETE_RESULT_OK)}
            )
        }
    }
}

const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1