package gr.tei.erasmus.pp.eventmate.ui.events

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.models.Event
import gr.tei.erasmus.pp.eventmate.ui.base.BaseViewModel

class EventsViewModel : BaseViewModel() {
	
	private val _eventState: MutableLiveData<EventProgressState> = MutableLiveData()
	val eventProgressState: LiveData<EventProgressState> = _eventState
	
	private val _events = MutableLiveData<MutableList<Event>>()
	//	val events: LiveData<MutableList<Event>> = _events
	var events = MutableLiveData<MutableList<Event>>()
	
	private var allEvents = mutableListOf<Event>()
	
	fun createEvent(newEvent: Event) {
//		list.addAll(events.value)
		allEvents.add(newEvent)
		// todo rest call
		events.value = allEvents
		allEvents
	}
	
	fun obtainEvents() {
		events
	}

	
}