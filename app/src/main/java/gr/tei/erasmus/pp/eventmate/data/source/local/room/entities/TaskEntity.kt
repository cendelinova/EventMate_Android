package gr.tei.erasmus.pp.eventmate.data.source.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "task")
data class TaskEntity(
	@PrimaryKey var uid: Long,
	@ForeignKey(
		entity = EventEntity::class,
		parentColumns = ["uid"],
		childColumns = ["event_id"],
		onDelete = ForeignKey.CASCADE
	)
	@ColumnInfo(name = "event_id") var eventId: Long,
	@ColumnInfo(name = "task_name") var taskName: String,
	@ColumnInfo(name = "points") var points: Int,
	@ColumnInfo(name = "comment") var description: String?,
	@ColumnInfo(name = "location") var place: String?,
	@ColumnInfo(name = "time_limit") var timeLimit: Int?
)