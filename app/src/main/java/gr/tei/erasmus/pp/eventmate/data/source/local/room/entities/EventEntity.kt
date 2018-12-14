package gr.tei.erasmus.pp.eventmate.data.source.local.room.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "event")
data class EventEntity(
	@PrimaryKey(autoGenerate = true) var uid: Long?,
	@ColumnInfo(name = "event_name") var eventName: String,
	@ColumnInfo(name = "date") var date: String,
	@ColumnInfo(name = "place") var place: String?,
	@ColumnInfo(name = "state") var state: String
)