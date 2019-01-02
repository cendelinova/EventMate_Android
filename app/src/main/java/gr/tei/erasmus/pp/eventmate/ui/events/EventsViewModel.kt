package gr.tei.erasmus.pp.eventmate.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.constants.Constants
import gr.tei.erasmus.pp.eventmate.constants.Constants.EventFilter.*
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.Event.EventState.UNDEFINED_STATE
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
	
	fun filterEvents(
		filterRole: Constants.EventFilter?,
		eventStateFilter: Event.EventState
	): MutableList<Event> {
		var filteredEvents: List<Event> = allEvents
		
		if (filterRole == UNDEFINED_FILTER && eventStateFilter == UNDEFINED_STATE) return allEvents
		
		if (filterRole == OWNER_FILTER && eventStateFilter != UNDEFINED_STATE) {
			filteredEvents = allEvents.filter { e -> e.eventOwner?.id == 1L && e.state == eventStateFilter.name }
		} else if (filterRole == GUEST_FILTER && eventStateFilter != UNDEFINED_STATE) {
			filteredEvents = allEvents.filter { e -> e.eventOwner?.id != 1L && e.state == eventStateFilter.name }
		} else if (filterRole == OWNER_FILTER) {
			filteredEvents = allEvents.filter { e -> e.eventOwner?.id == 1L }
		} else if (filterRole == GUEST_FILTER) {
			filteredEvents = allEvents.filter { e -> e.eventOwner?.id != 1L }
		} else if (filterRole == UNDEFINED_FILTER && eventStateFilter != UNDEFINED_STATE) {
			filteredEvents = allEvents.filter { e -> e.state == eventStateFilter.name }
		}
		
		val sharedPreferenceHelper = App.COMPONENTS.provideSharedPreferencesHelper()
		
		filterRole?.let {
			sharedPreferenceHelper.saveString(Constants.FILTER_EVENT_ROLE, it.name)
			
		}
		
		sharedPreferenceHelper.saveString(Constants.FILTER_EVENT_STATE, eventStateFilter.name)
		
		return filteredEvents.toMutableList()
	}
	
	data class EventListState(
		val events: MutableList<Event>,
		val deleteEventName: String? = null
	) : State()
	
}