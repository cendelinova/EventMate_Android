package gr.tei.erasmus.pp.eventmate.data.repository

import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.EventDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.EventEntity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class EventRepository(private val eventDao: EventDao) : CoroutineScope {
	private val job: Job = Job()
	override val coroutineContext: CoroutineContext = job + Dispatchers.IO
	
	fun getAllEvents(): Deferred<MutableList<Event>> =
		async {
			eventDao.getAll()
				.map {
					Event.convertToModel(it)
				}.toMutableList()
		}
	
	fun insert(eventEntity: EventEntity) = eventDao.insert(eventEntity)


//	fun getEventDetail(id: String): Deferred<Event> {
//
//	}
}
