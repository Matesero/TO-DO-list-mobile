package com.example.to_do_list_mobile

data class Task(
    val id: Int,
    var date: String,
    var description: String,
    var isCompleted : Boolean
)

data class TaskDTO(
    val date: String,
    val description: String,
    val isCompleted: Boolean
)