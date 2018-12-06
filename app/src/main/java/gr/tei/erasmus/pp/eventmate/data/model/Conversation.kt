package gr.tei.erasmus.pp.eventmate.data.model

data class Conversation(
	val id: Long?,
	val name: String,
	val photo: String,
	val lastMessageText: String,
	val lastMessageTime: String,
	val isGroup: Boolean
) {

}