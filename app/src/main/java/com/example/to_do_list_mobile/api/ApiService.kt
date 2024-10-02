import com.example.to_do_list_mobile.Task
import com.example.to_do_list_mobile.TaskDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApi {
    @POST("api/createTask")
    fun createTask(): Call<Void>

    @POST("api/addAll")
    fun addAllTasks(@Body list: ArrayList<TaskDTO>): Call<Void>

    @GET("api/getAll")
    fun getAllTasks(): Call<List<Task>>

    @GET("api/getOne/{id}")
    fun getTask(@Path("id") id: Int): Call<Task>

    @DELETE("api/deleteOne/{id}")
    fun deleteTask(@Path("id") id: Int): Call<Void>

    @DELETE("api/deleteAll")
    fun deleteAllTasks(): Call<Void>

    @PATCH("api/switchCompleted/{id}")
    fun switchCompleted(@Path("id") id: Int): Call<Void>

    @PATCH("api/changeDate/{id}")
    fun changeDate(@Path("id") id: Int, @Body date: String): Call<Void>

    @PATCH("api/changeDescription/{id}")
    fun changeDescription(@Path("id") id: Int, @Body description: String): Call<Void>
}
