package gr.tei.erasmus.pp.eventmate.data.source.remote.services

import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.data.model.TaskRequest
import gr.tei.erasmus.pp.eventmate.data.model.UserSubmissionPoints
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface TaskService {
	@GET("/me/event")
	fun getTasks(): Deferred<MutableList<Task>>
	
	@GET("/task/{id}")
	fun getTask(@Path("id") id: Long): Deferred<Response<Task>>
	
	@POST("/event/{id}/task")
	fun insertTask(@Path("id") id: Long, @Body task: TaskRequest): Deferred<Response<Task>>
	
	@GET("/event/{id}/tasks")
	fun getEventTasks(@Path("id") id: Long): Deferred<Response<MutableList<Task>>>
	
	@DELETE("/task/{id}")
	fun deleteTask(@Path("id") id: Long): Deferred<Response<MutableList<Task>>>
	
	@PUT("/task/id")
	fun updateTask(@Path("id") id: Long, @Body task: TaskRequest): Deferred<MutableList<Task>>
	
	@POST("/task/{id}/assignPoints")
	fun assignPoints(@Path("id") taskId: Long, @Body userSubmissionPointList: MutableList<UserSubmissionPoints>): Deferred<Response<Void>>
	
	@POST("/task{id}/pushState")
	fun changeTaskStatus(@Path("id") taskId: Long): Deferred<Response<Task>>
}