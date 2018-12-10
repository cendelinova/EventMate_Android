package gr.tei.erasmus.pp.eventmate.data.source.local.room.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "task")
data class TaskEntity(
	@PrimaryKey(autoGenerate = true) var uid: Long?,
	@ColumnInfo(name = "task_name") var taskName: String,
	@ColumnInfo(name = "points") var points: Int,
	@ColumnInfo(name = "description") var description: String,
	@ColumnInfo(name = "place") var place: String
)