package com.example.to_do_list_mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToDoListAdapter(
    private val toDoList: ArrayList<ToDo>,
    private val deleteCallback: (Int) -> Unit,
    private val editCallback: (ToDo, Any?) -> Unit
) : RecyclerView.Adapter<ToDoListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskDate: TextView = view.findViewById(R.id.date)
        val taskText: TextView = view.findViewById(R.id.description)
        val editButton: ImageButton = view.findViewById(R.id.editBtn)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.taskText.text = toDoList[position].description
        holder.taskDate.text = toDoList[position].date
        holder.editButton.setOnClickListener {
            editCallback(toDoList[position], position)
        }
        holder.deleteButton.setOnClickListener {
            deleteCallback(position)
        }
    }

    override fun getItemCount() = toDoList.size
}
