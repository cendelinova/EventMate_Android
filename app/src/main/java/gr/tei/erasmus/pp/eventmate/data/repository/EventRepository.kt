package gr.tei.erasmus.pp.eventmate.data.repository

import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.EventDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.EventEntity
import gr.tei.erasmus.pp.eventmate.helpers.RestHelper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class EventRepository(private val eventDao: EventDao) : CoroutineScope {
	private val job: Job = Job()
	override val coroutineContext: CoroutineContext = job + Dispatchers.IO
	
	private val restHelper = App.COMPONENTS.provideRestHelper()
	
	fun getAllEvents(): Deferred<MutableList<Event>> =
		async {
			eventDao.getAll()
				.map {
					Event.convertToModel(it)
				}.toMutableList()
		}
	
	fun insert(eventEntity: EventEntity) = eventDao.insert(eventEntity)
	
	fun delete(eventEntity: EventEntity) = eventDao.delete(eventEntity.uid!!)
	
	fun getEventsFromServer() = restHelper.getEvents()


//	fun getEventDetail(id: String): Deferred<Event> {
//
//	}
}
