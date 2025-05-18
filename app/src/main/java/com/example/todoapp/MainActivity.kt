package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.todoapp.network.RetrofitInstance
import com.example.todoapp.persistence.TodoDatabase
import com.example.todoapp.repository.TodoRepo
import com.example.todoapp.ui.Screens.TodoDetailScreen
import com.example.todoapp.ui.Screens.TodoListScreen
import com.example.todoapp.ui.theme.TodoAppTheme
import com.example.todoapp.viewmodel.TodoDetailViewModel
import com.example.todoapp.viewmodel.TodoListViewModel

class MainActivity : ComponentActivity() {

    private lateinit var todoRepository: TodoRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            "todo_database"
        ).build()

        val todoApi = RetrofitInstance.api
        val todoDao = database.getTodoDao()
        todoRepository = TodoRepo(todoApi, todoDao)

        setContent {
            TodoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    SetupNavGraph(navController, todoRepository)
                }
            }
        }
    }
}

@Composable
fun SetupNavGraph(navController: NavHostController, todoRepository: TodoRepo) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current

    NavHost(navController = navController, startDestination = "todo_list") {

        composable("todo_list") {
            viewModelStoreOwner?.let { owner ->
                val factory = TodoListViewModel.TodoListViewModelFactory(todoRepository)
                val viewModel = ViewModelProvider(owner, factory)[TodoListViewModel::class.java]
                TodoListScreen(navController = navController, viewModel = viewModel)
            }
        }

        composable(
            "todo_detail/{todoId}",
            arguments = listOf(navArgument("todoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getInt("todoId") ?: 0
            viewModelStoreOwner?.let { owner ->
                val factory = TodoDetailViewModel.TodoDetailViewModelFactory(todoRepository, todoId)
                val viewModel = ViewModelProvider(owner, factory)[TodoDetailViewModel::class.java]
                TodoDetailScreen(navController = navController, viewModel = viewModel)
            }
        }
    }
}
