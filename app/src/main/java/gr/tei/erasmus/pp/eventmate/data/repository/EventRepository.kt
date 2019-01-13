package gr.tei.erasmus.pp.eventmate.data.repository

import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.EventRequest
import gr.tei.erasmus.pp.eventmate.data.model.Invitation
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.EventDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.EventEntity
import timber.log.Timber


class EventRepository(private val eventDao: EventDao, private val taskRepository: TaskRepository) {
	
	private val restHelper = App.COMPONENTS.provideRestHelper()
	
	suspend fun getAllEvents(): MutableList<EventEntity> = eventDao.getAll()
	
	suspend fun getMyEvents(): MutableList<Event>? {
		val result = restHelper.getEvents().await()
		Timber.d("Get my Events $result")
		if (result.isSuccessful && result.body() != null) {
			// save events locally
			val events = result.body()!!.map { Event.convertToEntity(it) }
			eventDao.insertAll(events)
			for (event in result.body()!!) {
				// save event's tasks locally
				event.tasks?.let { taskRepository.saveEventTasks(event) }
			}
		}
		
		return result.body()
	}
	
	suspend fun insert(event: EventRequest) {
		val result = restHelper.insertEvent(event).await()
		Timber.v("Result of adding new event $result")
		if (result.isSuccessful && result.body() != null) {
			eventDao.insert(Event.convertToEntity(result.body()!!))
		}
	}
	
	suspend fun update(event: EventRequest) {
		val result = restHelper.updateEvent(event)?.await()
		Timber.v("Result of updating new event $result")
		if (result?.isSuccessful == true && result.body() != null) {
			eventDao.update(Event.convertToEntity(result.body()!!))
		}
	}
	
	suspend fun delete(eventId: Long) {
		val result = restHelper.deleteEvent(eventId).await()
		Timber.v("Result of deleting  event $result")
		if (result.isSuccessful) {
			eventDao.delete(eventId)
		}
	}
	
	fun inviteGuests(eventId: Long, invitations: MutableList<Invitation>) = restHelper.inviteGuests(eventId, invitations)
	
	fun getEvent(eventId: Long) = restHelper.getEvent(eventId)
	
	
}
