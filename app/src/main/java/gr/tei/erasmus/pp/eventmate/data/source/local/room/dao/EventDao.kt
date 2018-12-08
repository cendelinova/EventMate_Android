package gr.tei.erasmus.pp.eventmate.data.source.local.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.EventEntity

@Dao
interface EventDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(eventEntity: EventEntity)
	
	@Query("SELECT * FROM event")
	fun getAll(): MutableList<EventEntity>
	
	@Delete
	fun delete(eventEntity: EventEntity)
}