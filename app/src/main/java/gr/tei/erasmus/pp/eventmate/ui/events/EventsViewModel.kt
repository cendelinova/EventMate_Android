package gr.tei.erasmus.pp.eventmate.ui.events

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.constants.Constants
import gr.tei.erasmus.pp.eventmate.constants.Constants.EventFilter.*
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.EventDetail
import gr.tei.erasmus.pp.eventmate.data.model.EventDetail.EventState.UNDEFINED_STATE
import gr.tei.erasmus.pp.eventmate.data.model.EventRequest
import gr.tei.erasmus.pp.eventmate.helpers.ErrorHelper
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class EventsViewModel : BaseViewModel() {
	private val eventRepository = App.COMPONENTS.provideEventRepository()
	private val userRepository = App.COMPONENTS.provideUserRepository()
	private val userRoleHelper = App.COMPONENTS.provideUserRoleHelper()
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	private var allEvents = mutableListOf<Event>()
	
	fun getEvents(showProgress: Boolean = true) {
		launch {
			if (showProgress) mStates.postValue(LoadingState)
			allEvents.clear()
			try {
				val response = eventRepository.getMyEvents().await()
				val state = if (response.isSuccessful && response.body() != null) {
					allEvents.addAll(response.body()!!)
					EventList2State(response.body()!!)
				} else {
					Timber.e(response.errorBody()?.string())
					ErrorState(Throwable(ErrorHelper.getErrorMessageFromHeader(response.headers())))
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
				val state = if (response.isSuccessful) {
					val event = allEvents.find { event -> event.id == eventId }
					allEvents.remove(event)
					EventList2State(allEvents, event?.name)
				} else {
					Timber.e(response.errorBody()?.string())
					ErrorState(Throwable(ErrorHelper.getErrorMessageFromHeader(response.headers())))
				}
				mStates.postValue(state)
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
					Timber.e(response.errorBody()?.string())
					ErrorState(Throwable(ErrorHelper.getErrorMessageFromHeader(response.headers())))
				}
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun getEventList() = allEvents
	
	fun getEvent(eventId: Long, showProgress: Boolean = true) {
		launch {
			if (showProgress) mStates.postValue(LoadingState)
			try {
				val response = eventRepository.getEvent(eventId).await()
				val state = if (response.isSuccessful && response.body() != null) {
					EventList2State(mutableListOf(response.body()!!))
				} else {
					Timber.e(response.errorBody()?.string())
					ErrorState(Throwable(ErrorHelper.getErrorMessageFromHeader(response.headers())))
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
					Timber.e(response.errorBody()?.string())
					ErrorState(Throwable(ErrorHelper.getErrorMessageFromHeader(response.headers())))
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
					ErrorState(Throwable(ErrorHelper.getErrorMessage(response?.code()!!)))
				}
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	
	fun filterEvents(
		filterRole: Constants.EventFilter?,
		eventStateFilter: EventDetail.EventState
	): MutableList<Event> {
		var filteredEvents: List<Event> = allEvents
		
		if (filterRole == UNDEFINED_FILTER && eventStateFilter == UNDEFINED_STATE) return allEvents
		
		
		if (filterRole == OWNER_FILTER && eventStateFilter != UNDEFINED_STATE) {
			filteredEvents =
					allEvents.filter { e -> userRoleHelper.isSameUser(e.eventOwner!!) && e.state == eventStateFilter.name }
		} else if (filterRole == GUEST_FILTER && eventStateFilter != UNDEFINED_STATE) {
			filteredEvents =
					allEvents.filter { e -> !userRoleHelper.isSameUser(e.eventOwner!!) && e.state == eventStateFilter.name }
		} else if (filterRole == OWNER_FILTER) {
			filteredEvents = allEvents.filter { e -> userRoleHelper.isSameUser(e.eventOwner!!) }
		} else if (filterRole == GUEST_FILTER) {
			filteredEvents = allEvents.filter { e -> !userRoleHelper.isSameUser(e.eventOwner!!) }
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
					EventListState(mutableListOf(response.body()!!))
				} else {
					Timber.e(response.errorBody()?.string())
					ErrorState(Throwable(ErrorHelper.getErrorMessageFromHeader(response.headers())))
				}
				
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	data class EventListState(
		val events: MutableList<EventDetail>,
		val deleteEventName: String? = null
	) : State()
	
	data class EventList2State(
		val events: MutableList<Event>,
		val deleteEventName: String? = null
	) : State()
	
}