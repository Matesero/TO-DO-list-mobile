package com.example.to_do_list_mobile

data class ToDo(
    val date: String,
    val description: String,
    val completed: Boolean,
    val index: Int)