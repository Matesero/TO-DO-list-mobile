import android.util.Log
import com.example.to_do_list_mobile.Task
import com.example.to_do_list_mobile.TaskDTO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TaskRepository {
    private val api: TaskApi
    var toDoList = ArrayList<Task>()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(TaskApi::class.java)
    }

    fun createNewTask() {
        api.createTask().enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                uploadFromDatabase()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error creating task: ${t.message}")
            }
        })
    }

    fun sendToDatabase(list: ArrayList<TaskDTO>) {
        api.addAllTasks(list).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                uploadFromDatabase()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error sending tasks: ${t.message}")
            }
        })
    }

    fun uploadFromDatabase() {
        api.getAllTasks().enqueue(object : retrofit2.Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: retrofit2.Response<List<Task>>) {
                if (response.isSuccessful) {
                    toDoList.clear();
                    response.body()?.let { tasks ->
                        for (task in tasks) {
                            Log.d("123", task.toString())
                            toDoList.add(task)
                        }
                    } ?: println("No tasks found.")
                } else {
                    println("Error with upload: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                println("Error fetching tasks: ${t.message}")
            }
        })
    }

    fun getTaskFromDatabase(id: Int, callback: (Task?) -> Unit) {
        api.getTask(id).enqueue(object : retrofit2.Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    println("Error fetching task: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                println("Error fetching task: ${t.message}")
                callback(null)
            }
        })
    }

    fun deleteOneTask(id: Int) {
        api.deleteTask(id).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                uploadFromDatabase()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error deleting task: ${t.message}")
            }
        })
    }

    fun deleteAllTasks() {
        api.deleteAllTasks().enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error deleting all tasks: ${t.message}")
            }
        })
    }

    fun switchCompleted(id: Int) {
        api.switchCompleted(id).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                uploadFromDatabase()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error switching task completion: ${t.message}")
            }
        })
    }

    fun changeDate(id: Int, date: String) {
        api.changeDate(id, date).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                Log.d("change date", date)
                uploadFromDatabase()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error changing task date: ${t.message}")
            }
        })
    }

    fun changeDescription(id: Int, description: String) {
        api.changeDescription(id, description).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                uploadFromDatabase()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error changing task description: ${t.message}")
            }
        })
    }
}
