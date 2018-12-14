package gr.tei.erasmus.pp.eventmate.data.repository

import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.TaskDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.TaskEntity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TaskRepository(private val taskDao: TaskDao) : CoroutineScope {
	private val job: Job = Job()
	override val coroutineContext: CoroutineContext = job + Dispatchers.IO
	
	fun getAllTasks(eventId: Long): Deferred<MutableList<Task>> =
		async {
			taskDao.getAllTasks(eventId)
				.map {
					Task.convertToModel(it)
				}.toMutableList()
		}
	
	fun getTask(taskId: Long): Deferred<Task> = async {
		Task.convertToModel(taskDao.getTask(taskId))
	}
	
	fun insert(taskEntity: TaskEntity) = taskDao.insert(taskEntity)
	
	fun delete(taskEntity: TaskEntity) = taskDao.delete(taskEntity.uid!!)
	
}