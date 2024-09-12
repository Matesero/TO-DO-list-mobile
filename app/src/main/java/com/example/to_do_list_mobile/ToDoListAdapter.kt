package com.example.to_do_list_mobile

import android.app.ActivityManager.TaskDescription
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ToDoListAdapter(
    private val toDoList: ArrayList<Task>,
    private val deleteCallback: (Int) -> Unit
) : RecyclerView.Adapter<ToDoListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val task: LinearLayout = view.findViewById(R.id.task)
        val taskDateView: TextView = view.findViewById(R.id.date_view)
        val taskDateEdit: EditText = view.findViewById(R.id.date_edit)
        val taskDescriptionView: TextView = view.findViewById(R.id.description_view)
        val taskDescriptionEdit: EditText = view.findViewById(R.id.description_edit)
        var completedSwitch: Switch = view.findViewById(R.id.completedSwitch)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = toDoList[position]

        holder.taskDescriptionView.text = task.description
        if (task.description !== "description") {
            holder.taskDescriptionEdit.setText(task.description)
        }

        holder.taskDescriptionView.setOnClickListener {
            holder.taskDescriptionView.visibility = View.GONE
            holder.taskDescriptionEdit.visibility = View.VISIBLE
            holder.taskDescriptionEdit.requestFocus()
        }

        holder.taskDescriptionEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val newDescription = holder.taskDescriptionEdit.text.toString()
                task.description = newDescription
                holder.taskDescriptionView.text = newDescription
                holder.taskDescriptionView.visibility = View.VISIBLE
                holder.taskDescriptionEdit.visibility = View.GONE
            }
        }

        holder.taskDateView.text = task.date
        holder.taskDateEdit.setText(task.date)

        holder.taskDateView.setOnClickListener {
            holder.taskDateView.visibility = View.GONE
            holder.taskDateEdit.visibility = View.VISIBLE
            holder.taskDateEdit.requestFocus()
        }

        holder.taskDateEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val newDate = holder.taskDateEdit.text.toString()
                if (checkDate(newDate) || newDate === "date") {
                    task.date = newDate
                    holder.taskDateView.text = newDate
                    holder.taskDateView.visibility = View.VISIBLE
                    holder.taskDateEdit.visibility = View.GONE
                } else {
                    holder.taskDateView.visibility = View.VISIBLE
                    holder.taskDateEdit.visibility = View.GONE
                    holder.taskDateEdit.setText(task.date)
                    Toast.makeText(holder.itemView.context, "Дата введена некорректно!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        holder.completedSwitch.isChecked = task.completed
        updateSwitch (
            holder.completedSwitch,
            holder.task,
            task.completed,
            holder.taskDateView,
            holder.taskDescriptionView,
            holder.itemView.context
        )

        holder.completedSwitch.setOnCheckedChangeListener { _, isChecked ->
            task.completed = isChecked
            updateSwitch (
                holder.completedSwitch,
                holder.task,
                isChecked,
                holder.taskDateView,
                holder.taskDescriptionView,
                holder.itemView.context
            )
        }

        holder.deleteButton.setOnClickListener {
            deleteCallback(position)
        }
    }

    override fun getItemCount(): Int {
        return toDoList.size
    }

    private fun updateSwitch (switch: Switch, task: LinearLayout, completed: Boolean, date: TextView, description: TextView, context: Context) {
        if (completed) {
            switch.thumbDrawable = ContextCompat.getDrawable(context, R.drawable.switch_thumb_on)
            switch.trackDrawable = ContextCompat.getDrawable(context, R.drawable.switch_track_on)
            task.background = ContextCompat.getDrawable(context, R.drawable.rounded_corner_light_green)
            date.paintFlags = date.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            description.paintFlags = description.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            switch.thumbDrawable = ContextCompat.getDrawable(context, R.drawable.switch_thumb)
            switch.trackDrawable = ContextCompat.getDrawable(context, R.drawable.switch_track)
            task.background = ContextCompat.getDrawable(context, R.drawable.rounded_corner_white)
            date.paintFlags = date.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            description.paintFlags = description.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkDate(date: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return try {
            LocalDate.parse(date, formatter)
            true
        } catch (e: Exception) {
            false
        }
    }
}
