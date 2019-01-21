package gr.tei.erasmus.pp.eventmate.data.model

data class Event(
	val id: Long,
	val name: String,
	val place: String,
	val date: String,
	val taskCount: Int,
	val usersCount: Int,
	val reportsCount: Int,
	val invitationsCount: Int,
	val photo: String?,
	val state: String,
	val eventOwner: User?
)


