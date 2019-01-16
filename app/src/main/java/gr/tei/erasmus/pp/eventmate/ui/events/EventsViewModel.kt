package gr.tei.erasmus.pp.eventmate.ui.events

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.constants.Constants
import gr.tei.erasmus.pp.eventmate.constants.Constants.EventFilter.*
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.Event.EventState.UNDEFINED_STATE
import gr.tei.erasmus.pp.eventmate.data.model.EventRequest
import gr.tei.erasmus.pp.eventmate.helpers.ErrorHelper
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class EventsViewModel : BaseViewModel() {
	private val eventRepository = App.COMPONENTS.provideEventRepository()
	private val userRepository = App.COMPONENTS.provideUserRepository()
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	private var allEvents = mutableListOf<Event>()
	
	fun getEvents() {
		launch {
			mStates.postValue(LoadingState)
			allEvents.clear()
			try {
				val response = eventRepository.getMyEvents().await()
				val state = if (response.isSuccessful && response.body() != null) {
					allEvents.addAll(response.body()!!)
					EventListState(response.body()!!)
				} else {
					Timber.e(response.errorBody()?.string())
					ErrorState(Throwable(ErrorHelper.getErrorMessage(response.code())))
				}
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun deleteEvent(eventId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = eventRepository.delete(eventId).await()
				
				if (response.isSuccessful) {
					val event = allEvents.find { event -> event.id == eventId }
					event?.let {
						allEvents.remove(it)
						mStates.postValue(EventListState(allEvents, it.name))
					}
					mStates.postValue(DeletedState)
				} else {
					Timber.e(response.errorBody()?.string())
					mStates.postValue(ErrorState(Throwable("Error")))
				}
				
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun createEvent(newEvent: EventRequest) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = eventRepository.insert(newEvent).await()
				val state = if (response.isSuccessful && response.body() != null) {
					FinishedState
				} else {
					ErrorState(Throwable("Error"))
				}
				mStates.postValue(state)
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
				val response = eventRepository.getEvent(eventId).await()
				val state = if (response.isSuccessful && response.body() != null) {
					EventListState(mutableListOf(response.body()!!))
				} else {
					Timber.e(response.errorBody()?.string())
					ErrorState(Throwable("Error during getting event"))
				}
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun getAppUsers() {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = userRepository.getAppUsers().await()
				val state = if (response.isSuccessful && response.body() != null) {
					UserViewModel.AppUserState(response.body()!!)
				} else {
					ErrorState(Throwable(response.message()))
				}
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun updateEvent(updatedEvent: EventRequest) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = eventRepository.update(updatedEvent)?.await()
				val state = if (response?.isSuccessful == true && response?.body() != null) {
					FinishedState
				} else {
					Timber.e(response?.errorBody()?.string())
					ErrorState(Throwable("Error"))
				}
				mStates.postValue(state)
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
	
	fun changeEventStatus(eventId: Long, context: Context) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = eventRepository.changeEventStatus(eventId).await()
				val state = if (response.isSuccessful && response.body() != null) {
					val eventState = Event.EventState.valueOf(response.body()!!.state)
					if (eventState == Event.EventState.READY_TO_PLAY) ReadyToPlayEvent(response.body()!!)
					else {
						EventListState(mutableListOf(response.body()!!))
					}
				} else {
					Timber.e(response.errorBody()?.string())
					
					ErrorState(Throwable("Error"))
				}
				
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	data class ReadyToPlayEvent(val event: Event) : State()
	
	data class EventListState(
		val events: MutableList<Event>,
		val deleteEventName: String? = null
	) : State()
	
}