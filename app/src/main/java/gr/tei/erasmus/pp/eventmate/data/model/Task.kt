package gr.tei.erasmus.pp.eventmate.data.model

import gr.tei.erasmus.pp.eventmate.R
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
	val taskState: String,
	val assignees: List<User>?,
	val taskOwner: User,
	val submissionsCount: Int,
	var checked: Boolean = false,
	val parentEventState: String
) {

//	constructor(
//		id: Long,
//		name: String
//	) : this(
//		id, 0, name, 0, null, null, null, null, null
//	)
//
//	constructor(
//		id: Long,
//		eventId: Long,
//		name: String,
//		points: Int,
//		description: String?,
//		place: String?,
//		timeLimit: Int?
//	) : this(
//		id, eventId, name, points, description, place, timeLimit, null, null
//	)
	
	
	companion object {
		fun convertToModel(entity: TaskEntity) = with(entity) {
			//			Task(
//				uid,
//				eventId,
//				taskName,
//				points,
//				description,
//				place,
//				timeLimit,
//				null, null
//			)
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
	
	enum class TaskState(
		val taskItemStatusMessage: Int,
		val taskDetailStatusMessage: Int,
		val fabIcon: Int,
		val taskDetailIcon: Int,
		val taskToastMessage: Int
	) {
		EDITABLE(
			R.string.task_item_status_editable,
			R.string.task_detail_status_editable,
			R.drawable.ic_lock,
			R.drawable.ic_edit_solid,
			R.string.task_locked
		),
		READY_TO_START(
			R.string.task_item_status_ready_to_play,
			R.string.task_detail_status_ready_to_play,
			R.drawable.ic_play_button,
			R.drawable.ic_start_task,
			R.string.task_ready_to_play
		),
		IN_PLAY(
			R.string.task_item_status_in_play,
			R.string.task_detail_status_in_play,
			R.drawable.ic_stop,
			R.drawable.ic_play_button_16dp,
			R.string.task_started
		),
		IN_REVIEW(
			R.string.task_item_status_in_review,
			R.string.task_detail_status_in_review,
			R.drawable.ic_evaluation,
			R.drawable.ic_task_under_evaluation,
			R.string.task_ready_to_evaluation
		),
		FINISHED(
			R.string.task_item_status_finished,
			R.string.task_detail_status_finished,
			R.drawable.ic_undefined,
			R.drawable.ic_task_finish,
			R.string.task_finished
		)
	}
}
