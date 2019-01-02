package gr.tei.erasmus.pp.eventmate.constants

class Constants {
	companion object {
		const val EVENT_ID = "eventId"
		const val TASK_ID = "taskId"
		const val USER_ID = "userId"
	}
	
	enum class EVENT_FILTER(val text: String) {
		GUEST_FILTER("Guest"),
		OWNER_FILTER ("Owner"),
		UNDEFINED_FILTER("Undefined")
	}
}