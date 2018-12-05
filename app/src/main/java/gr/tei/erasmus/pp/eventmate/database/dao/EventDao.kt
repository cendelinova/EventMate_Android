package gr.tei.erasmus.pp.eventmate.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import gr.tei.erasmus.pp.eventmate.database.entities.EventEntity

@Dao
interface EventDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(eventEntity: EventEntity)
	
	@Query("SELECT * FROM event")
	fun getAll(): LiveData<MutableList<EventEntity>>
}