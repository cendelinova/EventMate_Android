package gr.tei.erasmus.pp.eventmate.data.model

import gr.tei.erasmus.pp.eventmate.R
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
	val reports: MutableList<ReportResponse>?,
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
	
	
	enum class EventState(val statusMessage: Int, val iconFab: Int, val messageResource: Int) {
		EDITABLE(R.string.event_status_editable, R.drawable.ic_lock, R.string.event_locked),
		READY_TO_PLAY(R.string.event_status_ready_to_play, R.drawable.ic_play_button, R.string.event_started),
		IN_PLAY(R.string.event_status_in_play, R.drawable.ic_stop, R.string.event_stopped),
		UNDER_EVALUATION(R.string.event_status_evaluation, R.drawable.ic_evaluation, R.string.event_evaluated),
		FINISHED(R.string.event_status_finished, R.drawable.ic_event_summary, R.string.event_summary_requested),
		UNDEFINED_STATE(R.string.event_status_undefined, R.drawable.ic_undefined, R.string.event_status_undefined)
	}
	
}


