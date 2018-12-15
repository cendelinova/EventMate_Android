package gr.tei.erasmus.pp.eventmate.data.source.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "invitation")
data class InvitationEntity(
	@PrimaryKey(autoGenerate = true) var uid: Long?,
	@ForeignKey(
		entity = EventEntity::class,
		parentColumns = ["uid"],
		childColumns = ["event_id"],
		onDelete = ForeignKey.CASCADE
	)
	@ColumnInfo(name = "event_id") var eventId: Long,
	@ColumnInfo(name = "user_name") var userName: String,
	@ColumnInfo(name = "state") var date: String
)