package gr.tei.erasmus.pp.eventmate.data.repository

import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.EventDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.TaskDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.EventEntity
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.TaskEntity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TaskRepository(private val taskDao: TaskDao) : CoroutineScope {
	private val job: Job = Job()
	override val coroutineContext: CoroutineContext = job + Dispatchers.IO
	
	fun getAllTasks(): Deferred<MutableList<Task>> =
		async {
			taskDao.getAll()
				.map {
					Task.convertToModel(it)
				}.toMutableList()
		}
	
	fun insert(taskEntity: TaskEntity) = taskDao.insert(taskEntity)
	
	fun delete(taskEntity: TaskEntity) = taskDao.delete(taskEntity.uid!!)
	
}