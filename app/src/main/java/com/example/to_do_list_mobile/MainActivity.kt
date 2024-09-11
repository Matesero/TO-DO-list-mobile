package com.example.to_do_list_mobile

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do_list_mobile.databinding.ActivityMainBinding
import java.lang.Exception
import java.time.LocalDate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    private lateinit var toDoListView: RecyclerView
    private lateinit var toDoListAdapter: ToDoListAdapter
    private lateinit var addBtn: ImageButton
    private lateinit var binding: ActivityMainBinding
    private var toDoList = ArrayList<Task>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        addBtn = findViewById(R.id.addBtn)
        addBtn.setOnClickListener{
            showAddTodoDialog();
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.download->{
                    download("toDoList", toDoList)
                    true
                }
                R.id.upload->{
                    upload()
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
            toDoList
        ) { position ->
            toDoList.removeAt(position)
            toDoListAdapter.notifyItemRemoved(position)
            toDoListAdapter.notifyItemRangeChanged(position, toDoList.size)
        }

        toDoListView = findViewById(R.id.toDoList)
        toDoListView.layoutManager = LinearLayoutManager(this)
        toDoListView.adapter = toDoListAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showAddTodoDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val editDate = dialogView.findViewById<EditText>(R.id.editTextDate)
        val editDescription = dialogView.findViewById<EditText>(R.id.editTextDescription)
        val error = dialogView.findViewById<TextView>(R.id.error);
        val saveBtn = dialogView.findViewById<Button>(R.id.saveBtn)
        val closeBtn = dialogView.findViewById<Button>(R.id.closeBtn)

        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Add task")

        val dialog = builder.create()
        dialog.show()

        saveBtn.setOnClickListener {
            var description = editDescription.text.toString()
            if (description.isEmpty()) {
                description = "description";
            }

            var date = editDate.text.toString()
            if (date.isEmpty()) {
                date = "date";
            }

            if (checkDate(date) || date === "date") {
                val newTask = Task(date, description)
                toDoList.add(newTask)
                toDoListAdapter.notifyItemInserted(toDoList.size - 1)
                dialog.dismiss()
            } else {
                error.text = "Enter date correctly"
            }
        }

        closeBtn.setOnClickListener {
            dialog.dismiss()
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

    private fun download(fileName: String, data: Any) {
        val json = convertToJson(data)
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir,"${fileName}_${System.currentTimeMillis()}.json")
        file.writeText(json)
    }

    private fun convertToJson(data: Any): String {
        return Gson().toJson(data)
    }

    private fun upload(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/json"
        try {
            startActivityForResult(Intent.createChooser(intent, "select file"), 100)
        } catch (e: Exception) {
            return
        }
    }

    @Deprecated("Deprecated in java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            val uri: Uri? = data.data
            val path: String = uri?.path.toString()
            val file = File(path)
            Log.d("Download", file.toString())
            if (uri != null) {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    val jsonString = inputStream.bufferedReader().use { it.readText() }
                    val newToDoList = jsonToArray(jsonString)
                    displayToDoList(newToDoList)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun jsonToArray(file: String): ArrayList<Task> {
        val gson = Gson();
        val taskType = object : TypeToken<ArrayList<Task>>() {}.type
        return gson.fromJson(file, taskType)
    }

    private fun reset() {
        toDoList.clear()
        toDoListAdapter.notifyDataSetChanged()
    }

    private fun displayToDoList(newToDoList: ArrayList<Task>){
        reset()
        for (i in 0..<newToDoList.size){
            val task = newToDoList[i]
            toDoList.add(task)
            toDoListAdapter.notifyDataSetChanged()
        }
    }
}