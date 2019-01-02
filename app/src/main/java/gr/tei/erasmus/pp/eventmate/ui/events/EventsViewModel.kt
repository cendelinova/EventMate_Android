package gr.tei.erasmus.pp.eventmate.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.EventRequest
import gr.tei.erasmus.pp.eventmate.ui.base.*
import kotlinx.coroutines.launch

class EventsViewModel : BaseViewModel() {
	private val eventRepository by lazy { App.COMPONENTS.provideEventRepository() }
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	private var allEvents = mutableListOf<Event>()
	
	fun getEvents() {
		launch {
			mStates.postValue(LoadingState)
			allEvents.clear()
			try {
				val events = eventRepository.getMyEvents()
				events?.run {
					allEvents.addAll(this)
					mStates.postValue(EventListState(this))
				}
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun deleteEvent(eventId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				eventRepository.delete(eventId)
				val event = allEvents.find { event -> event.id == eventId }
				event?.let {
					allEvents.remove(it)
					mStates.postValue(EventListState(allEvents, it.name))
				}
				mStates.postValue(DeletedState)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun createEvent(newEvent: EventRequest) {
		launch {
			mStates.postValue(LoadingState)
			try {
				eventRepository.insert(newEvent)
				mStates.postValue(FinishedState)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun getEventList() = allEvents
	
	fun getEvent(eventId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val event = Event.convertToModel(eventRepository.getEvent(eventId))
				mStates.postValue(EventListState(mutableListOf(event)))
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun updateEvent(updatedEvent: EventRequest) {
		launch {
			mStates.postValue(LoadingState)
			try {
				eventRepository.update(updatedEvent)
				mStates.postValue(FinishedState)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	data class EventListState(
		val events: MutableList<Event>,
		val deleteEventName: String? = null
	) : State()
	
}