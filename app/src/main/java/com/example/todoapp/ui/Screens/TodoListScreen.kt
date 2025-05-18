package com.example.todoapp.ui.Screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.viewmodel.TodoListViewModel
import com.example.todoapp.model.Todo
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@Composable
fun TodoListScreen(navController: NavHostController, viewModel: TodoListViewModel = viewModel()) {
    val todos: List<Todo> by viewModel.todos.collectAsState()
    TodoListScreenContent(navController = navController, todos = todos)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreenContent(navController: NavHostController, todos: List<Todo>){
    val scrollBehavior = rememberTopAppBarState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My To-do List", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
        content = { padding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                color = MaterialTheme.colorScheme.background
            ) {
                if (todos.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(todos) { todo ->
                            TodoItem(todo = todo, onItemClick = {
                                navController.navigate("todo_detail/${todo.id}")
                            })
                        }
                    }
                }
            }
        }
    )
}
// Composable function for displaying an individual Todo item in the list
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItem(todo: Todo, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = onItemClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = contentColorFor(MaterialTheme.colorScheme.surfaceVariant)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = todo.title,
                modifier = Modifier.weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            var checkState by remember { mutableStateOf(todo.completed) }

            Checkbox(
                checked = checkState,
                onCheckedChange = { isChecked ->
                    checkState = isChecked
                },
                enabled = true,
            )
        }
    }
}
