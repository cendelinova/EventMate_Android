package gr.tei.erasmus.pp.eventmate.data.model

data class EventResponse(
	val id: Long,
	val name: String,
	val place: String,
	val date: String,
	val taskCount: Int,
	val usersCount: Int,
	val reportsCount: Int,
	val invitationsCount: Int,
	val photo: String?,
	val tasks: MutableList<Task>?,
	val state: Event.EventState,
	val reports: MutableList<Report>,
	val invitations: MutableList<Invitation>,
	val guests: MutableList<User>,
	val eventOwner: User,
	val permissions: MutableList<Permission>
)