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
	val reports: MutableList<Report>?,
	val photo: String?
) {
	
	constructor(name: String) : this(
		null, name, null, "", "", null,
		EventState.EDITABLE, null, null
	)
	
	constructor(id: Long?, name: String, date: String, place: String) : this(
		id, name, null, date, place, null,
		EventState.EDITABLE, null, null
	)
	
	constructor(id: Long?, name: String, date: String, place: String, photo: String) : this(
		id, name, null, date, place, null,
		EventState.EDITABLE, null, photo
	)
	
	companion object {
		fun convertToModel(entity: EventEntity) = with(entity) {
			Event(
				uid,
				eventName,
				DateTimeHelper.convertDateTimeToString(date, DateTimeHelper.FULL_DATE_TIME_FORMAT),
				place
			)
		}
		
		fun convertToEntity(model: Event) = with(model) {
			EventEntity(
				id,
				name,
				DateTimeHelper.parseDateTimeFromString(date, DateTimeHelper.FULL_DATE_TIME_FORMAT),
				place
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

