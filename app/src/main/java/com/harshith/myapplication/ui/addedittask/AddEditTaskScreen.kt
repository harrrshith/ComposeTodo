package com.harshith.myapplication.ui.addedittask

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harshith.myapplication.R
import com.harshith.myapplication.ui.theme.ComposeTodoTheme
import com.harshith.myapplication.util.AddEditTaskTopAppBar

@Composable
fun AddEditTaskScreen(
    @StringRes topBarTitle: Int,
    onTaskUpdate: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditTaskViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = { AddEditTaskTopAppBar(title = topBarTitle, onBack = onBack) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::saveTask
            ) {
                Icon(Icons.Filled.Done, stringResource(id = R.string.save_tasks))
            }
        }
    ) {paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        AddEditTaskContent(
            isLoading = uiState.isLoading,
            title = uiState.title,
            description = uiState.description,
            onTitleChange =  viewModel::updateTitle,
            onDescriptionChange = viewModel::updateDescription,
            modifier = Modifier.padding(paddingValues)
        )
        LaunchedEffect(uiState.isTaskSaved){
            if(uiState.isTaskSaved){
                onTaskUpdate()
            }
        }
        uiState.userMessage?.let { userMessage ->
            val snackBarText = stringResource(id = userMessage)
            LaunchedEffect(scaffoldState, viewModel, userMessage, snackBarText){
                scaffoldState.snackbarHostState.showSnackbar(snackBarText)
                viewModel.snackBarShown()
            }
        }

    }
}

@Composable
fun AddEditTaskContent(
    isLoading: Boolean,
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val textFieldColor = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.secondary.copy(alpha = ContentAlpha.high)
        )

        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.task_title),
                    style = MaterialTheme.typography.bodySmall,
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal),
            maxLines = 1,
            colors = textFieldColor
        )

        OutlinedTextField(
            value = description,
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(),
            onValueChange = onDescriptionChange,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.task_description),
                    style = MaterialTheme.typography.bodySmall)
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal),
            maxLines = 1,
            colors = textFieldColor
        )

    }

}

@Preview
@Composable
fun AddEditTaskScreenPreview(){
    ComposeTodoTheme {
        Surface {
            AddEditTaskScreen(
                topBarTitle = R.string.add_task,
                onTaskUpdate = {},
                onBack = {},
            )
        }
    }
}