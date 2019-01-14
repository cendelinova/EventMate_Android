package gr.tei.erasmus.pp.eventmate.data.model

data class EventRequest(
	var id: Long? = null,
	var name: String = "",
	var date: String = "",
	var place: String? = null,
	var photo: String? = null,
	var state: String = Event.EventState.EDITABLE.name,
	var invitations: List<Invitation> = mutableListOf()
)