package gr.tei.erasmus.pp.eventmate.data.source.local.room.entities

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class EventWithTasks {
	@Embedded
	lateinit var eventEntity: EventEntity
	
	@Relation(parentColumn = "uid", entityColumn = "event_id", entity = TaskEntity::class)
	var tasks: List<TaskEntity>? = null
	
}