package gr.tei.erasmus.pp.eventmate.data.model

data class EventRequest(
	val id: Long? = null,
	val name: String,
	val date: String,
	val place: String?,
	val photo: String?
)