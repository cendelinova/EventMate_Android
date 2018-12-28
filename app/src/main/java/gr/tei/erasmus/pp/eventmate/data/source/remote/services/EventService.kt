package gr.tei.erasmus.pp.eventmate.data.source.remote.services

import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.EventRequest
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface EventService {
	@GET("/me/events")
	fun getEvents(): Deferred<Response<MutableList<Event>>>
	
	@GET("/event/{id}")
	fun getEvent(@Path("id") id: Long): Deferred<Event>
	
	@POST("/event")
	fun insertEvent(@Body event: EventRequest): Deferred<Response<Event>>
	
	@PUT("/event/{id}")
	fun updateEvent(@Path("id") id: Long, @Body event: EventRequest): Deferred<Response<Event>>
}