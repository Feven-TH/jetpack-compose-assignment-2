package com.example.todoapp.ui.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.todoapp.model.Todo
import com.example.todoapp.viewmodel.TodoDetailViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun TodoDetailScreen(
    navController: NavHostController,
    viewModel: TodoDetailViewModel
) {
    val todo: Todo? by produceState<Todo?>(initialValue = null, key1 = viewModel.todoId) {
        viewModel.todo.collect {
            value = it
        }
    }

    if (todo == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        TodoDetailScreenContent(navController = navController, todo = todo!!)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreenContent(navController: NavHostController, todo: Todo) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo Details", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { padding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                color = MaterialTheme.colorScheme.background
            ) {
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = contentColorFor(MaterialTheme.colorScheme.surfaceVariant)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Title: ${todo.title}", style = MaterialTheme.typography.headlineSmall)
                        Text("ID: ${todo.id}", style = MaterialTheme.typography.bodyLarge)
                        Text("User ID: ${todo.userId}", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            "Completed: ${if (todo.completed) "Yes" else "No"}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    )
}


