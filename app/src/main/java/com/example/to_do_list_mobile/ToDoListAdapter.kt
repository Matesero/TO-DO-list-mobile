package com.example.to_do_list_mobile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ToDoListAdapter(
    private val toDoList: ArrayList<Task>,
    private val deleteCallback: (Int) -> Unit
) : RecyclerView.Adapter<ToDoListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskDate: TextView = view.findViewById(R.id.date)
        val taskText: TextView = view.findViewById(R.id.description)
        var completedSwitch: Switch = view.findViewById(R.id.completedSwitch)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = toDoList[position]
        holder.taskText.text = task.description
        holder.taskDate.text = task.date
        holder.completedSwitch.isChecked = task.completed
        updateSwitch(holder.completedSwitch, task.completed, holder.itemView.context)
        holder.completedSwitch.setOnCheckedChangeListener { _, isChecked ->
            task.completed = isChecked
            updateSwitch(holder.completedSwitch, isChecked, holder.itemView.context)
        }
        holder.deleteButton.setOnClickListener {
            deleteCallback(position)
        }
    }

    override fun getItemCount(): Int {
        return toDoList.size
    }

    private fun updateSwitch (switch: Switch, completed: Boolean, context: Context) {
        if (completed) {
            switch.thumbDrawable = ContextCompat.getDrawable(context, R.drawable.switch_thumb_on)
            switch.trackDrawable = ContextCompat.getDrawable(context, R.drawable.switch_track_on)
        } else {
            switch.thumbDrawable = ContextCompat.getDrawable(context, R.drawable.switch_thumb)
            switch.trackDrawable = ContextCompat.getDrawable(context, R.drawable.switch_track)
        }
    }
}
