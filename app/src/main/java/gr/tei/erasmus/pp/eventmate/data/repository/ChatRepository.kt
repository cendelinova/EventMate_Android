package gr.tei.erasmus.pp.eventmate.data.repository

import android.content.Context
import gr.tei.erasmus.pp.eventmate.data.model.ChatMessage
import gr.tei.erasmus.pp.eventmate.helpers.RestHelper

class ChatRepository(private val context: Context, private val restHelper: RestHelper) {
	fun saveMessage(chatMessage: ChatMessage) = restHelper.saveMessage(chatMessage)
	fun getMyConversations() = restHelper.getMyConversations()
	fun getConversationDetail(userId: Long) = restHelper.getConversationDetail(userId)
}