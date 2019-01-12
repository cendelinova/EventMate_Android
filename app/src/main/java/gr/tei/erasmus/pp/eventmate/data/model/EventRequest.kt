package gr.tei.erasmus.pp.eventmate.data.model

data class EventRequest(
	var id: Long? = null,
	var name: String = "",
	var date: String = "",
	var place: String? = null,
	var photo: String? = null,
	var invitations: List<Invitation> = mutableListOf()
)