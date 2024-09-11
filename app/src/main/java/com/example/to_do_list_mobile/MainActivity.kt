package com.example.to_do_list_mobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var toDoListView: RecyclerView
    private lateinit var toDoListAdapter: ToDoListAdapter
    private lateinit var addBtn: ImageButton
    private var toDoList = ArrayList<ToDo>()
    private var editPosition: Int = -1
    private var count = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addBtn = findViewById(R.id.addBtn)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.download->{
                    Toast.makeText(this,"download", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.upload->{
                    Toast.makeText(this,"upload", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.reset->{
                    reset()
                    true
                }
                else->{
                    false
                }
            }
        }

        toDoListAdapter = ToDoListAdapter(
            toDoList,
            { position ->
                toDoList.removeAt(position)
                toDoListAdapter.notifyItemRemoved(position)
            }
        ) { toDo, position ->
            editPosition = position as Int
        }

        toDoListView = findViewById(R.id.toDoList)
        toDoListView.layoutManager = LinearLayoutManager(this)
        toDoListView.adapter = toDoListAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun reset() {
        toDoList.clear()
        toDoListAdapter.notifyDataSetChanged()
        editPosition = -1
        count = 0
    }

}