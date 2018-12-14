package gr.tei.erasmus.pp.eventmate.data.source.local.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.InvitationEntity

@Dao
interface InvitationDao {
	@Query("SELECT * FROM invitation WHERE event_id = :eventId")
	fun getAllInvitation(eventId: Long): MutableList<InvitationEntity>
}