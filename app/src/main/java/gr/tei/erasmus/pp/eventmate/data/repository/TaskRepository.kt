package gr.tei.erasmus.pp.eventmate.data.repository

import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.data.model.TaskRequest
import gr.tei.erasmus.pp.eventmate.data.model.UserSubmissionPoints
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.TaskDao
import gr.tei.erasmus.pp.eventmate.helpers.RestHelper
import timber.log.Timber

class TaskRepository(private val restHelper: RestHelper, private val taskDao: TaskDao) {
	
//	suspend fun getAllTasks(eventId: Long): MutableList<Task> = taskDao.getAllTasks(eventId)
//		.map {
//			Task.convertToModel(it)
//		}.toMutableList()
	
	fun getEventTasks(eventId: Long) = restHelper.getEventTasks(eventId)
//	fun getTask(taskId: Long): Task = Task.convertToModel(taskDao.getTask(taskId))
	
	fun getTask(taskId: Long) = restHelper.getTask(taskId)
	
	suspend fun insert(task: TaskRequest) = restHelper.insertTask(task)
//	{
//		val result = restHelper.insertTask(task).await()
////		Timber.v("Result of adding new task $result")
////		if (result.isSuccessful && result.body() != null) {
////			Timber.v("Success add new task ${result.body()}")
////			Task.convertToEntity(result.body()!!).also {
////				taskDao.insert(it)
////			}
////		}
//	}

//	fun delete(taskEntity: TaskEntity) = taskDao.delete(taskEntity.uid)
	
	fun assignPoints(taskId: Long, userSubmissionPointList: MutableList<UserSubmissionPoints>) = restHelper.assignPoints(taskId, userSubmissionPointList)
	
	fun deleteTask(taskId: Long) = restHelper.deleteTask(taskId)
	
	fun saveEventTasks(event: Event) {
		val tasks = event.tasks?.map { Task.convertToEntity(it) }
		tasks?.let {
			taskDao.insertAll(it)
		}
	}
	
	fun changeTaskStatus(taskId: Long) = restHelper.changeTaskStatus(taskId)
	
}