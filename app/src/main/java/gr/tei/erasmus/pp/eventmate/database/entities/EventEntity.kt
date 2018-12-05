package gr.tei.erasmus.pp.eventmate.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.joda.time.DateTime

@Entity(tableName = "event")
data class EventEntity(
	@PrimaryKey(autoGenerate = true) var uid: Int?,
	@ColumnInfo(name = "event_name") var eventName: String?,
	@ColumnInfo(name = "date") var date: DateTime
)