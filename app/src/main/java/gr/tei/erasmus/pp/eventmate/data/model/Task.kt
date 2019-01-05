package gr.tei.erasmus.pp.eventmate.data.model

import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.TaskEntity

data class Task(
	val id: Long,
	val eventId: Long,
	val name: String,
	val points: Int,
	val description: String?,
	val place: String?,
	val timeLimit: Int?,
	val photo: String?,
	private val assigness: List<User>?,
	var checked: Boolean = false
) {
	
	constructor(
		id: Long,
		name: String
	) : this(
		id, 0, name, 0, null, null, null, null, null
	)
	
	constructor(
		id: Long,
		eventId: Long,
		name: String,
		points: Int,
		description: String?,
		place: String?,
		timeLimit: Int?
	) : this(
		id, eventId, name, points, description, place, timeLimit, null, null
	)
	
	
	companion object {
		fun convertToModel(entity: TaskEntity) = with(entity) {
			Task(
				uid,
				eventId,
				taskName,
				points,
				description,
				place,
				timeLimit,
				null, null
			)
		}
		
		fun convertToEntity(model: Task) = with(model) {
			TaskEntity(
				id,
				eventId,
				name,
				points,
				description,
				place,
				timeLimit
			)
		}
	}
	
	enum class TaskState {
		EDITABLE,
		READY_TO_PLAY,
		UNDER_EVALUATION,
		FINISHED
	}
}
