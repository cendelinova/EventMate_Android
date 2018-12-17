package gr.tei.erasmus.pp.eventmate.helpers

import gr.tei.erasmus.pp.eventmate.data.model.EventRequest
import gr.tei.erasmus.pp.eventmate.data.model.TaskRequest
import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.data.source.remote.services.EventService
import gr.tei.erasmus.pp.eventmate.data.source.remote.services.TaskService
import gr.tei.erasmus.pp.eventmate.data.source.remote.services.UserService
import retrofit2.Retrofit

class RestHelper(retrofit: Retrofit) {
	
	// Retrofit services
	private val eventService: EventService = retrofit.create(EventService::class.java)
	private val userService: UserService = retrofit.create(UserService::class.java)
	private val taskService = retrofit.create(TaskService::class.java)
	
	fun getEvents() = eventService.getEvents()
	fun insertEvent(event: EventRequest) = eventService.insertEvent(event)
	
	fun insertTask(task: TaskRequest) = taskService.insertTask(task.eventId, task)
	fun getTask(taskId: Long) = taskService.getTask(taskId)
	
	fun getUser(userId: Long) = userService.getUser(userId)
	fun registerUser(user: User) = userService.registerUser(user)
	
	
}