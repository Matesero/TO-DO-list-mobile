package com.example.to_do_list_mobile

data class Task(
    var date: String,
    var description: String,
    var completed: Boolean = false
)