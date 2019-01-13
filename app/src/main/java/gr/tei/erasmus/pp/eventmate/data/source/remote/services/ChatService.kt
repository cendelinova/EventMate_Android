package gr.tei.erasmus.pp.eventmate.data.source.remote.services

import gr.tei.erasmus.pp.eventmate.data.model.Message
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatService {
	@POST("/message")
	fun saveMessage(@Body message: Message)
	
	
}