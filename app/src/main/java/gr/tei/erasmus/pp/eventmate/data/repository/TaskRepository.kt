package gr.tei.erasmus.pp.eventmate.data.repository

import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.data.model.TaskRequest
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.TaskDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.TaskEntity
import gr.tei.erasmus.pp.eventmate.helpers.RestHelper
import timber.log.Timber

class TaskRepository(private val restHelper: RestHelper, private val taskDao: TaskDao) {
	
	suspend fun getAllTasks(eventId: Long): MutableList<Task> = taskDao.getAllTasks(eventId)
		.map {
			Task.convertToModel(it)
		}.toMutableList()
	
	fun getTask(taskId: Long): Task = Task.convertToModel(taskDao.getTask(taskId))
	
	suspend fun getTaskFromServer(taskId: Long) = restHelper.getTask(taskId)
	
	suspend fun insert(task: TaskRequest) {
		val result = restHelper.insertTask(task).await()
		if (result.isSuccessful && result.body() != null) {
			Timber.v("Success add new task ${result.body()}")
			Task.convertToEntity(result.body()!!).apply {
				eventId = task.eventId
			}.also {
				taskDao.insert(it)
			}
		}
	}
	
	fun delete(taskEntity: TaskEntity) = taskDao.delete(taskEntity.uid!!)
	
}