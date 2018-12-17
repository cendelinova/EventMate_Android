package gr.tei.erasmus.pp.eventmate.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.ui.base.BaseViewModel
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import kotlinx.coroutines.launch
import timber.log.Timber

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
//				val events = eventRepository.getAllEvents().await()
				val result = eventRepository.getMyEvents().await()
				Timber.v("result je ${result.body()}")
				if (result.isSuccessful && result.body() != null) {
					val events = result.body()!!
					allEvents.addAll(events)
					mStates.postValue(EventListState(events))
				}

			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun deleteEvent(event: Event) {
		launch {
			mStates.postValue(LoadingState)
			try {
//				eventRepository.delete(Event.convertToEntity(event))
				allEvents.remove(event)
				mStates.postValue(EventListState(allEvents))
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun getEventList() = allEvents
	
	fun addToEventList(deletedItem: Event, deletedIndex: Int) {
		allEvents.add(deletedIndex, deletedItem)
		mStates.postValue(EventListState(allEvents))
	}
	
	fun getEvent(eventId: Long) {
//		launch {
//			mStates.postValue(LoadingState)
//			allEvents.clear()
//			try {
//				val event = Event.convertToModel(eventRepository.getEvent(eventId))
//				mStates.postValue(EventListState(mutableListOf(event)))
//			} catch (error: Throwable) {
//				mStates.postValue(ErrorState(error))
//			}
//		}
	}
	
	data class EventListState(val events: MutableList<Event>) : State() {
		companion object {
			fun from(list: MutableList<Event>): EventListState {
				return if (list.isEmpty()) error("event list should not be empty")
				else {
					EventListState(list)
				}
			}
		}
	}
}