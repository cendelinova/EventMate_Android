package gr.tei.erasmus.pp.eventmate.models

import org.joda.time.DateTime

data class Event(
	val id: String, val name: String, val permissions: MutableList<Permission>?, val date: DateTime, val place: String,
	val tasks: MutableList<Task>?, val state: EventState, val reports: MutableList<Report>?
) {
	
	// todo prepsat date na string, time na string
	
	constructor(name: String) : this("", name, null, DateTime.now(), "", null, EventState.EDITABLE, null)
	constructor(name: String, date: DateTime) : this("", name, null, date, "", null, EventState.EDITABLE, null)
	
	enum class EventState {
		EDITABLE,
		READY_TO_PLAY,
		UNDER_EVALUATION,
		FINISHED
	}
	
}

