package gr.tei.erasmus.pp.eventmate.data.source.remote.services

import gr.tei.erasmus.pp.eventmate.data.model.Event
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventService {
	@GET("/me/event")
	fun getEvents(): Deferred<MutableList<Event>>
	
	@GET("/event/{id}")
	fun getEvent(@Path("id") id: Long): Deferred<Event>
	
	@POST
	fun insertEvent(@Body event: Event): Deferred<Response<Unit>>
}