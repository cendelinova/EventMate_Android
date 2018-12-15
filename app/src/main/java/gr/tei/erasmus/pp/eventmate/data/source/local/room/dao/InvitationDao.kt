package gr.tei.erasmus.pp.eventmate.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.InvitationEntity

@Dao
interface InvitationDao {
	@Query("SELECT * FROM invitation WHERE event_id = :eventId")
	fun getAllInvitation(eventId: Long): MutableList<InvitationEntity>
}