package gr.tei.erasmus.pp.eventmate.constants

class Constants {
	companion object {
		const val EVENT_ID = "eventId"
		const val TASK_ID = "taskId"
		const val USER_ID = "userId"
		const val FILTER_EVENT_STATE = "eventStateFilter"
		const val FILTER_EVENT_ROLE = "eventRoleFilter"
		const val VIDEO = "video"
		const val AUDIO = "audio"
		const val PHOTO = "photo"
		const val SUBMISSION_EXTRA = "submissionExtra"
		const val EVENT_EDITABLE = "eventEditable"
	}
	
	enum class EventFilter(val text: String) {
		GUEST_FILTER("Guest"),
		OWNER_FILTER ("Owner"),
		UNDEFINED_FILTER("Undefined")
	}
}