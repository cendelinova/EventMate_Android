package gr.tei.erasmus.pp.eventmate.constants

class Constants {
	companion object {
		const val EVENT_ID = "eventId"
		const val TASK_ID = "taskId"
		const val USER_ID = "userId"
		const val USER_MAIL = "userEmail"
		const val USER_PASSWORD = "userPassword"
		const val FILTER_EVENT_STATE = "eventStateFilter"
		const val FILTER_EVENT_ROLE = "eventRoleFilter"
		const val VIDEO = "video"
		const val AUDIO = "audio"
		const val PHOTO = "photo"
		const val SUBMISSION_EXTRA = "submissionExtra"
		const val EVENT_ADD_TASKS = "eventAddTasks"
		const val EVENT_ADD_GUESTS = "eventAddGuests"
		const val EVENT_STATE = "eventState"
		const val USER_NAME = "userName"
		const val EVENT_SHOW_MENU = "eventShowMenu"
		const val TASK_SHOW_MENU = "taskShowMenu"
	}
	
	enum class EventFilter(val text: String) {
		GUEST_FILTER("Guest"),
		OWNER_FILTER ("Owner"),
		UNDEFINED_FILTER("Undefined")
	}
}