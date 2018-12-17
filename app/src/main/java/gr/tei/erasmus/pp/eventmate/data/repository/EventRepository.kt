package gr.tei.erasmus.pp.eventmate.data.repository

import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.EventRequest
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.EventDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.EventEntity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class EventRepository(private val eventDao: EventDao) : CoroutineScope {
	private val job: Job = Job()
	override val coroutineContext: CoroutineContext = job + Dispatchers.IO
	
	private val restHelper by lazy { App.COMPONENTS.provideRestHelper() }
	
//	fun getAllEvents(): Deferred<MutableList<Event>> =
//		async {
//			eventDao.getAll()
//				.map {
//					Event.convertToModel(it)
//				}.toMutableList()
//		}
	
	suspend fun getMyEvents() = restHelper.getEvents()
	
	suspend fun insert(event: EventRequest) {
		val result = restHelper.insertEvent(event).await()
		if (result.isSuccessful && result.body() != null) {
//			eventDao.insert(Event.convertToEntity(result.body()!!))
		}
	}
	
	fun delete(eventEntity: EventEntity) = eventDao.delete(eventEntity.uid!!)
	
	fun getEventsFromServer() = restHelper.getEvents()
	
	fun getEvent(eventId: Long) = eventDao.getEvent(eventId)
	
	
}
