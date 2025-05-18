package com.example.todoapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import com.example.todoapp.repository.TodoRepo
import com.example.todoapp.model.Todo


// ViewModel for the TodoDetailScreen
class TodoDetailViewModel(
    private val todoRepository: TodoRepo,
    val todoId: Int
) : ViewModel() {

    val todo: StateFlow<Todo?> = todoRepository.getTodoById(todoId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    class TodoDetailViewModelFactory(
        private val todoRepository: TodoRepo,
        private val todoId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TodoDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TodoDetailViewModel(todoRepository, todoId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
