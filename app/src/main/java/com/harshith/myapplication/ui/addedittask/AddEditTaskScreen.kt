package com.harshith.myapplication.ui.addedittask

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.harshith.myapplication.R
import com.harshith.myapplication.ui.theme.ComposeTodoTheme
import com.harshith.myapplication.util.AddEditTaskTopAppBar

@Composable
fun AddEditTaskScreen(
    @StringRes topBarTitle: Int,
    onTaskUpdate: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditTaskViewModel = hiltViewModel()
    //TODO:ScaffoldState
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { AddEditTaskTopAppBar(title = topBarTitle, onBack = onBack) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Icon(Icons.Filled.Done, stringResource(id = R.string.save_tasks))
            }
        }
    ) {paddingValues ->
        AddEditTaskContent(
            isLoading = false,
            title = stringResource(id = R.string.task_title),
            description = stringResource(id = R.string.task_description),
            onTitleChange = {},
            onDescriptionChange = {},
            modifier = Modifier.padding(paddingValues)
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskContent(
    isLoading: Boolean,
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier
){
    if(isLoading){
        //TODO:Swipe Layout
    }else{
        Column(
            modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val textFieldColor = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.surface.copy(alpha = 1f)
            )
            
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.task_title),
                        style = MaterialTheme.typography.headlineMedium)
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                colors = textFieldColor
            )

            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                modifier = Modifier
                    .height(350.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.task_description),
                        style = MaterialTheme.typography.headlineMedium)
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                colors = textFieldColor
            )

        }
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
                //viewModel =
            )
        }
    }
}