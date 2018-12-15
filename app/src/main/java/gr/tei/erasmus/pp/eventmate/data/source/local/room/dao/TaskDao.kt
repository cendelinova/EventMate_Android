package gr.tei.erasmus.pp.eventmate.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.TaskEntity

@Dao
interface TaskDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(taskEntity: TaskEntity)
	
	@Query("SELECT * FROM task WHERE event_id = :eventId")
	fun getAllTasks(eventId: Long): MutableList<TaskEntity>
	
	@Query("SELECT count(*) FROM task WHERE event_id = :eventId ")
	fun getTaskCount(eventId: Long): Int
	
	@Query("DELETE FROM task WHERE uid = :uid")
	fun delete(uid: Long)
	
	@Query("SELECT * FROM task WHERE uid = :taskId ")
	fun getTask(taskId: Long) : TaskEntity
}