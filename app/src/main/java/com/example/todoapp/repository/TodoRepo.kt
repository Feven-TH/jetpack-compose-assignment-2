package com.example.todoapp.repository

import com.example.todoapp.model.Todo
import com.example.todoapp.network.TodoApi
import com.example.todoapp.persistence.TodoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.take
import java.lang.Exception

class TodoRepo(
    private val todoApi: TodoApi,
    private val todoDao: TodoDao
) {

    val todos: Flow<List<Todo>> = flow {
        // Emit the cached data first
        val cachedTodos = todoDao.getAll().catch { /* ignore */ }.take(1)
        emitAll(cachedTodos)

        // Fetch the latest data from the network
        try {
            val networkTodos = todoApi.getTodos()
            // Update the cache with the new data
            todoDao.deleteAll()
            todoDao.insertAll(networkTodos)
            // Emit the updated data
            emitAll(todoDao.getAll())
        } catch (e: Exception) {
            val todos: Flow<List<Todo>> = flow {
                emitAll(todoDao.getAll().take(1))

                try {
                    val networkTodos = todoApi.getTodos()
                    todoDao.deleteAll()
                    todoDao.insertAll(networkTodos)

                    emitAll(todoDao.getAll())
                } catch (e: Exception) {
                    println("Failed to fetch from network: ${e.message}")
                }
            }.catch { e ->
                println("Flow error in todos: ${e.message}")
                emit(emptyList())
            }
        }
    }

    fun getTodoById(id: Int): Flow<Todo> {
        return todoDao.getById(id)
    }

}
