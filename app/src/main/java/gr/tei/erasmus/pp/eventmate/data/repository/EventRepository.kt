package gr.tei.erasmus.pp.eventmate.data.repository

import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.EventRequest
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.EventDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.EventEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


class EventRepository(private val eventDao: EventDao) {
	
	private val restHelper by lazy { App.COMPONENTS.provideRestHelper() }
	
	suspend fun getAllEvents(): MutableList<EventEntity> = eventDao.getAll()
	
	suspend fun getMyEvents(): MutableList<Event>? {
		val result = restHelper.getEvents().await()
		if (result.isSuccessful && result.body() != null) {
			val events = result.body()!!.map { Event.convertToEntity(it) }
			eventDao.insertAll(events)
		}
		
		return result.body()
	}
	
	suspend fun insert(event: EventRequest) {
		val result = restHelper.insertEvent(event).await()
		if (result.isSuccessful && result.body() != null) {
			eventDao.insert(Event.convertToEntity(result.body()!!))
		}
	}
	
	fun delete(eventEntity: EventEntity) = eventDao.delete(eventEntity.uid!!)
	
	
	fun getEvent(eventId: Long) = eventDao.getEvent(eventId)
	
	
}
