package com.example.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Todo
import com.example.todoapp.repository.TodoRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class TodoListViewModel(
    private val todoRepository: TodoRepo
) : ViewModel() {

    val todos: StateFlow<List<Todo>> = todoRepository.todos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Factory to create the ViewModel with the repository dependency
    class TodoListViewModelFactory(
        private val todoRepository: TodoRepo
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TodoListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TodoListViewModel(todoRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
