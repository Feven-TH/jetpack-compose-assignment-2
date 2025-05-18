package com.example.todoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

// Data class to represent a TODO item.
@Serializable
@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @SerializedName("userId") val userId: Int,
    val title: String,
    val completed: Boolean,
)

