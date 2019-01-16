package gr.tei.erasmus.pp.eventmate.data.source.remote.services

import gr.tei.erasmus.pp.eventmate.data.model.User
import gr.tei.erasmus.pp.eventmate.data.model.UserRequest
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
	@GET("/user/{id}")
	fun getUser(@Path("id") id: Long): Deferred<Response<User>>
	
	@POST("/public/login")
	fun loginUser(@Body user: UserRequest): Deferred<Response<User>>
	
	@GET("/me")
	fun getMyProfile(): Deferred<Response<User>>
	
	@POST("/public/register")
	fun registerUser(@Body user: UserRequest): Deferred<Response<User>>
	
	@GET("/event/{eventId}/guests")
	fun getEventGuests(@Path("eventId") eventId: Long): Deferred<Response<MutableList<User>>>
	
	@GET("/public/users")
	fun getAllUsers(): Deferred<Response<MutableList<User>>>
}