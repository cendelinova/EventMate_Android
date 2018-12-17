package gr.tei.erasmus.pp.eventmate.data.model

import com.google.gson.annotations.Expose

data class TaskRequest(
	@Expose(serialize = false, deserialize = false)
	val eventId: Long,
	val name: String,
	val place: String?,
	val description: String?,
	val points: Int,
	val timeLimit: Int?,
	val photo: String?
)