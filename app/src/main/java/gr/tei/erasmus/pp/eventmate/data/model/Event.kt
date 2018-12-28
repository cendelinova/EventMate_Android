package gr.tei.erasmus.pp.eventmate.data.model

import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.EventEntity

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
	val tasks: MutableList<Task>?,
	val reports: MutableList<Report>?,
	val invitations: MutableList<Invitation>?,
	val guests: MutableList<User>?,
	val eventOwner: User?,
	val permissions: MutableList<Permission>?
) {
	constructor(id: Long, name: String, date: String, place: String) : this(
		id, name, place, date, 0, 0,
		0, 0, null, null, null, null, null, null, null
	)
	
	companion object {
		fun convertToModel(entity: EventEntity) = with(entity) {
			Event(
				uid,
				eventName,
				date,
				place
			)
		}
		
		fun convertToEntity(model: Event) = with(model) {
			EventEntity(
				id,
				name,
				date,
				place,
				"bla",
				taskCount,
				usersCount
			)
		}
	}
	
	enum class EventState {
		EDITABLE,
		READY_TO_PLAY,
		UNDER_EVALUATION,
		FINISHED
	}
	
}

