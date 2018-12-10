package gr.tei.erasmus.pp.eventmate.data.model

import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.EventEntity
import gr.tei.erasmus.pp.eventmate.helpers.DateTimeHelper

data class Event(
	val id: Long?,
	val name: String,
	val permissions: MutableList<Permission>?,
	val date: String,
	val place: String,
	val tasks: MutableList<Task>?,
	val state: EventState,
	val reports: MutableList<Report>?
) {
	
	constructor(name: String) : this(
		null, name, null, "", "", null,
		EventState.EDITABLE, null
	)
	
	constructor(id: Long?, name: String, date: String) : this(
		id, name, null, date, "", null,
		EventState.EDITABLE, null
	)
	
	companion object {
		fun convertToModel(entity: EventEntity) = with(entity) {
			Event(
				uid,
				eventName,
				DateTimeHelper.convertDateTimeToString(date, DateTimeHelper.DATE_TIME_FORMAT)
			)
		}
		
		fun convertToEntity(model: Event) = with(model) {
			EventEntity(
				id,
				name,
				DateTimeHelper.parseDateTimeFromString(date, DateTimeHelper.DATE_TIME_FORMAT)
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

