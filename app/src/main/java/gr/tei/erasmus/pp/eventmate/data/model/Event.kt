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
	val state: String,
	val tasks: MutableList<Task>?,
	val reports: MutableList<Report>?,
	val invitations: MutableList<Invitation>?,
	val guests: MutableList<User>?,
	val eventOwner: User?,
	val permissions: MutableList<Permission>?
) {
	constructor(
		id: Long,
		name: String,
		date: String,
		place: String,
		tasksCount: Int,
		usersCount: Int,
		photo: String?
	) : this(
		id, name, place, date, tasksCount, usersCount,
		0, 0, photo, EventState.READY_TO_PLAY.name, null, null, null, null, null, null
	)
	
	companion object {
		fun convertToModel(entity: EventEntity) = with(entity) {
			Event(
				uid,
				eventName,
				date,
				place,
				tasksCount,
				usersCount,
				photo ?: photo
			)
		}
		
		fun convertToEntity(model: Event) = with(model) {
			EventEntity(
				id,
				name,
				date,
				place,
				state,
				taskCount,
				usersCount,
				photo ?: photo
			)
		}
	}
	
	enum class EventState(val text: String) {
		EDITABLE("Editable"),
		READY_TO_PLAY("Ready to play"),
		IN_PLAY("In play"),
		UNDER_EVALUATION("Under evaluation"),
		FINISHED("Finished"),
		UNDEFINED_STATE("Undefined")
	}
	
}

