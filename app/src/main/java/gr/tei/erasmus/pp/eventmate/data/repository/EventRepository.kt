package gr.tei.erasmus.pp.eventmate.data.repository

import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.EventRequest
import gr.tei.erasmus.pp.eventmate.data.model.Invitation
import gr.tei.erasmus.pp.eventmate.data.source.local.room.dao.EventDao
import gr.tei.erasmus.pp.eventmate.data.source.local.room.entities.EventEntity


class EventRepository(private val eventDao: EventDao, private val taskRepository: TaskRepository) {
	
	private val restHelper = App.COMPONENTS.provideRestHelper()
	
	suspend fun getAllEvents(): MutableList<EventEntity> = eventDao.getAll()
	
	fun getMyEvents() = restHelper.getEvents()
	
//	{
//		val result = restHelper.getEvents()
//		Timber.d("Get my Events $result")
//		if (result.isSuccessful && result.body() != null) {
//			// save events locally
//			val events = result.body()!!.map { EventDetail.convertToEntity(it) }
//			eventDao.insertAll(events)
//			for (event in result.body()!!) {
//				// save event's tasks locally
//				event.tasks?.let { taskRepository.saveEventTasks(event) }
//			}
//		}
//
//		return result.body()
//	}
	
	fun insert(event: EventRequest) =  restHelper.insertEvent(event)
//	{
//		val result =
//		Timber.v("Result of adding new event $result")
//		if (result.isSuccessful && result.body() != null) {
//			eventDao.insert(EventDetail.convertToEntity(result.body()!!))
//		}
//	}
	
	fun update(event: EventRequest) = restHelper.updateEvent(event)
//		if (result?.isSuccessful == true && result.body() != null) {
//			eventDao.update(EventDetail.convertToEntity(result.body()!!))
//		}
	
	 fun delete(eventId: Long) = restHelper.deleteEvent(eventId)
// {
//		val result =
//		Timber.v("Result of deleting  event $result")
//		if (result.isSuccessful) {
//			eventDao.delete(eventId)
//		}
//	}
	
	fun inviteGuests(eventId: Long, invitations: MutableList<Invitation>) = restHelper.inviteGuests(eventId, invitations)
	
	fun getEvent(eventId: Long) = restHelper.getEvent(eventId)
	fun changeEventStatus(eventId: Long) = restHelper.changeEventStatus(eventId)
	
	
}
