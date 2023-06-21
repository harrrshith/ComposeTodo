package com.harshith.myapplication

import android.util.Log
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.harshith.myapplication.TodoDestinationArgs.TASK_ID_ARG
import com.harshith.myapplication.TodoDestinationArgs.TITLE_ARG
import com.harshith.myapplication.TodoDestinationArgs.USER_MSG_ARG
import com.harshith.myapplication.TodoScreens.ADD_EDIT_TASK_SCREEN
import com.harshith.myapplication.TodoScreens.STATISTICS_SCREEN
import com.harshith.myapplication.TodoScreens.TASKS_SCREEN
import com.harshith.myapplication.TodoScreens.TASK_DETAIL_SCREEN

private object TodoScreens{
    const val  TASKS_SCREEN = "tasks"
    const val STATISTICS_SCREEN = "statistics"
    const val TASK_DETAIL_SCREEN = "task"
    const val ADD_EDIT_TASK_SCREEN = "addEditTask"
}

object TodoDestinationArgs{
    const val USER_MSG_ARG = "userMessage"
    const val TASK_ID_ARG = "taskId"
    const val TITLE_ARG = "title"
}

object TodoDestination{
    const val TASK_ROUTE = "$TASKS_SCREEN?$USER_MSG_ARG={$USER_MSG_ARG}"
    const val STATISTICS_ROUTE = STATISTICS_SCREEN
    const val TASK_DETAILS_ROUTE = "$TASK_DETAIL_SCREEN/{$TASK_ID_ARG}"
    const val ADD_EDIT_TASK_ROUTE = "$ADD_EDIT_TASK_SCREEN/{$TITLE_ARG}?$TASK_ID_ARG={$TASK_ID_ARG}"
}

class TodoNavigationAction(private val navController: NavHostController){

    fun navigateToTasks(userMessage: Int = 0){
        val navigatesFromDrawer = userMessage == 0
        navController.navigate(
            TASKS_SCREEN.let {
                if(userMessage != 0)
                    "$it?$USER_MSG_ARG=$userMessage"
                else
                    it
            }
        ){
            popUpTo(navController.graph.findStartDestination().id){
                inclusive = !navigatesFromDrawer
                saveState = navigatesFromDrawer
            }
            launchSingleTop = true
            restoreState = navigatesFromDrawer
        }
    }

    fun navigateToStatistics(){
        navController.navigate(TodoDestination.STATISTICS_ROUTE){
            popUpTo(navController.graph.findStartDestination().id){
                saveState = true
            }
            launchSingleTop = true
            restoreState =  true
        }
    }

    fun navigateToTaskDetails(taskId: String?){
        navController.navigate("$TASK_DETAIL_SCREEN/$taskId"){
        Log.e("Response", "$taskId")
        }
    }

    fun navigateToAddEditTask(title: Int, taskId: String?){
        navController.navigate(
            "$ADD_EDIT_TASK_SCREEN/$title".let {
                if(taskId != null)
                    "$it?$TASK_ID_ARG=$taskId"
                else
                    it
            }
        )
    }
}