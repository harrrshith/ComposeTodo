package com.harshith.myapplication.ui.satistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harshith.myapplication.R
import com.harshith.myapplication.ui.theme.ComposeTodoTheme
import com.harshith.myapplication.util.StatisticsTaskTopAppBar

@Composable
fun StatisticsScreen(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            StatisticsTaskTopAppBar(
                openDrawer = openDrawer
            )
        }
    ) {paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        StatisticsScreenContent(
            loading = uiState.isLoading,
            empty = uiState.isEmpty,
            activeTaskPercentage = uiState.activeTasksPercent,
            completeTaskPercentage = uiState.completedTaskPercent,
            modifier.padding(paddingValues)
        )

    }

}

@Composable
fun StatisticsScreenContent(
    loading: Boolean,
    empty: Boolean,
    activeTaskPercentage: Float,
    completeTaskPercentage: Float,
    modifier: Modifier = Modifier
){
    Surface(
        modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(empty){
                Text(
                    text = stringResource(id = R.string.no_tasks),
                    style = MaterialTheme.typography.bodyLarge
                )
            }else{
                ShowText(
                    R.string.active_tasks,
                    activeTaskPercentage
                )
                ShowText(
                    R.string.completed_tasks,
                    completeTaskPercentage
                )
            }
            
        }

    }

}

@Composable
fun ShowText(
    title: Int = R.string.active,
    percent: Float = 100f
){
    Row {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = " = $percent%",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }

}

@Preview
@Composable
fun StatisticsScreenPreview(){
    ComposeTodoTheme {
        Surface {
            StatisticsScreenContent(
                loading = false,
                empty = false,
                activeTaskPercentage = 50F,
                completeTaskPercentage = 50F
            )
        }
    }
}