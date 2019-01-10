package gr.tei.erasmus.pp.eventmate.data.source.remote.services

import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.data.model.TaskRequest
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskService {
	@GET("/me/event")
	fun getTasks(): Deferred<MutableList<Task>>
	
	@GET("/task/{id}")
	fun getTask(@Path("id") id: Long): Deferred<Task>
	
	@POST("/event/{id}/task")
	fun insertTask(@Path("id") id: Long, @Body task: TaskRequest): Deferred<Response<Task>>
	
	@GET("/event/{id}/tasks")
	fun getEventTasks(@Path("id") id: Long): Deferred<Response<MutableList<Task>>>
}