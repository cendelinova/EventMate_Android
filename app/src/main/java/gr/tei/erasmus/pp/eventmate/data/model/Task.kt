package gr.tei.erasmus.pp.eventmate.data.model

import android.support.design.widget.TextInputLayout
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.TaskEntity
import gr.tei.erasmus.pp.eventmate.helpers.DateTimeHelper

data class Task(val id: Long?, val name: String, val points: Int, val description: String, val place: String, private val assigness: List<User>?) {
	
	companion object {
		fun convertToModel(entity: TaskEntity) = with(entity) {
			Task(
				uid,
				taskName,
				points,
				description,
				place,
				null
			)
		}
		
		fun convertToEntity(model: Task) = with(model) {
			TaskEntity(
				id,
				name,
				points,
				description,
				place
			)
		}
		
//		fun createTask(inputs: MutableList<TextInputLayout>) {
//			for (input in inputs) {
//
//			}
//		}
	}
	
	enum class TaskState {
		EDITABLE,
		READY_TO_PLAY,
		UNDER_EVALUATION,
		FINISHED
	}
}
