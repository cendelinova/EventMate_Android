package gr.tei.erasmus.pp.eventmate.data.source.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class EventEntity(
	@PrimaryKey(autoGenerate = true) var uid: Long?,
	@ColumnInfo(name = "event_name") var eventName: String,
	@ColumnInfo(name = "date") var date: String,
	@ColumnInfo(name = "location") var place: String?,
	@ColumnInfo(name = "state") var state: String
)