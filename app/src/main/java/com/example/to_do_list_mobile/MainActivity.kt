package com.example.to_do_list_mobile

import TaskRepository
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do_list_mobile.databinding.ActivityMainBinding
import java.lang.Exception
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var toDoListView: RecyclerView
    private lateinit var toDoListAdapter: ToDoListAdapter
    private lateinit var addBtn: ImageButton
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskRepository: TaskRepository;
    private var toDoList = ArrayList<Task>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        taskRepository = TaskRepository()

        addBtn = findViewById(R.id.addBtn)
        addBtn.setOnClickListener{
            taskRepository.createNewTask();
            displayToDoList();
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.download->{
                    download("toDoList", taskRepository.toDoList)
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
            toDoList,
            taskRepository
        ) { position ->
            toDoList.removeAt(position)
            toDoListAdapter.notifyItemRemoved(position)
            toDoListAdapter.notifyItemRangeChanged(position, toDoList.size)
        }

        toDoListView = findViewById(R.id.toDoList)
        toDoListView.layoutManager = LinearLayoutManager(this)
        toDoListView.adapter = toDoListAdapter

        displayToDoList()
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
            reset();
            startActivityForResult(Intent.createChooser(intent, "select file"), 100)
        } catch (e: Exception) {
            return
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            val uri: Uri? = data.data
            val path: String = uri?.path.toString()

            if (uri != null) {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    val jsonString = inputStream.bufferedReader().use { it.readText() }
                    val newToDoList = jsonToArray(jsonString);
                    var newTaskDTOList = ArrayList<TaskDTO>();
                    for (task in newToDoList) {
                        val taskDTO = TaskDTO(task.date, task.description, task.isCompleted);
                        newTaskDTOList.add(taskDTO);
                    }
                    taskRepository.sendToDatabase(newTaskDTOList);
                    displayToDoList();
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

    @SuppressLint("NotifyDataSetChanged")
    private fun reset() {
        taskRepository.deleteAllTasks();
        toDoListAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayToDoList(){
        taskRepository.uploadFromDatabase();
        toDoList.clear()
        toDoList.addAll(taskRepository.toDoList)
        toDoListAdapter.notifyDataSetChanged()
        Log.d("after clear", toDoList.toString())
    }
}