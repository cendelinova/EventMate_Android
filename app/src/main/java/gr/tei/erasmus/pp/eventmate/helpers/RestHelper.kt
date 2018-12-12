package gr.tei.erasmus.pp.eventmate.helpers

import gr.tei.erasmus.pp.eventmate.data.source.remote.services.EventService
import gr.tei.erasmus.pp.eventmate.data.source.remote.services.TaskService
import retrofit2.Retrofit

class RestHelper(retrofit: Retrofit) {
	
	// Retrofit services
	private val eventService: EventService = retrofit.create(EventService::class.java)
//	private val taskService = retrofit.create(TaskService::class.java)
	
	fun getEvents() = eventService.getEvents()
	
}