package gr.tei.erasmus.pp.eventmate.models

import org.joda.time.DateTime

data class Event(
	val id: String, val name: String, val permissions: MutableList<Permission>, val date: DateTime, val place: String,
	val tasks: MutableList<Task>, val state: EventState, val reports: MutableList<Report>
) {

	enum class EventState {
		EDITABLE,
		READY_TO_PLAY,
		UNDER_EVALUATION,
		FINISHED
	}
	
}

