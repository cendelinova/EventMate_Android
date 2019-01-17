package gr.tei.erasmus.pp.eventmate.data.source.remote.services

import gr.tei.erasmus.pp.eventmate.data.model.ChatMessage
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatService {
	@POST("/message")
	fun saveMessage(@Body message: ChatMessage): Deferred<Response<MutableList<ChatMessage>>>
	
	@GET("/me/messages/last")
	fun getMyConversations(): Deferred<Response<MutableList<ChatMessage>>>
	
	@GET("/me/messages/user/{id}")
	fun getConversationDetail(@Path("id") userId: Long): Deferred<Response<MutableList<ChatMessage>>>
	
}