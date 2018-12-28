package gr.tei.erasmus.pp.eventmate.data.source.local.room.dao

import androidx.room.*
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.EventEntity

@Dao
interface EventDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(eventEntity: EventEntity)
	
	@Update
	fun update(eventEntity: EventEntity)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	@JvmSuppressWildcards
	fun insertAll(eventEntity: List<EventEntity>)
	
	@Query("SELECT * FROM event")
	fun getAll(): MutableList<EventEntity>
	
	@Query("DELETE FROM event WHERE uid = :uid")
	fun delete(uid: Long)
	
	@Query("SELECT * FROM event WHERE uid = :eventId")
	fun getEvent(eventId: Long): EventEntity
}