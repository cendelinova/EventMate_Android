package gr.tei.erasmus.pp.eventmate.data.source.local.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.EventEntity

@Dao
interface EventDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(eventEntity: EventEntity)
	
	@Query("SELECT * FROM event")
	fun getAll(): MutableList<EventEntity>
	
	@Query("DELETE FROM event WHERE uid = :uid")
	fun delete(uid: Long)
	
	@Query("SELECT * FROM event WHERE uid = :eventId")
	fun getEvent(eventId: Long): EventEntity
}