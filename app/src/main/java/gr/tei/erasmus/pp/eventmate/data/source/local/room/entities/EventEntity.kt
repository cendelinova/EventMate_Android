package gr.tei.erasmus.pp.eventmate.data.source.local.room.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.joda.time.DateTime

@Entity(tableName = "event")
data class EventEntity(
	@PrimaryKey(autoGenerate = true) var uid: Long?,
	@ColumnInfo(name = "event_name") var eventName: String,
	@ColumnInfo(name = "date") var date: DateTime?,
	@ColumnInfo(name = "place") var place: String
//	@ColumnInfo(typeAffinity = ColumnInfo.BLOB, name = "photo") var image: ByteArray?
)