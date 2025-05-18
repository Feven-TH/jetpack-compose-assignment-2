package com.example.todoapp.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp.model.Todo


@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun getTodoDao(): TodoDao
}
