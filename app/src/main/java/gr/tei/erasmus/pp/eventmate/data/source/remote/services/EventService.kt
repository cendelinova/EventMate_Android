package gr.tei.erasmus.pp.eventmate.data.source.remote.services

import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.EventRequest
import gr.tei.erasmus.pp.eventmate.data.model.Invitation
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface EventService {
	@GET("/me/events")
	fun getEvents(): Deferred<Response<MutableList<Event>>>
	
	@GET("/event/{id}")
	fun getEvent(@Path("id") id: Long): Deferred<Response<Event>>
	
	@POST("/event")
	fun insertEvent(@Body event: EventRequest): Deferred<Response<Event>>
	
	@PUT("/event/{id}")
	fun updateEvent(@Path("id") id: Long, @Body event: EventRequest): Deferred<Response<Event>>
	
	@DELETE("/event/{id}")
	fun deleteEvent(@Path("id") id: Long): Deferred<Response<Void>>
	
	@POST("/event/{id}/invitation/list")
	fun inviteGuests(@Path("id") id: Long, @Body invitations: MutableList<Invitation>): Deferred<Response<Event>>
}