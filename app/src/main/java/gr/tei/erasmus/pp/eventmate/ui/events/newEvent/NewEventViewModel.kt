package gr.tei.erasmus.pp.eventmate.ui.events.newEvent

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.database.entities.EventEntity
import gr.tei.erasmus.pp.eventmate.models.Event
import gr.tei.erasmus.pp.eventmate.ui.base.BaseViewModel
import gr.tei.erasmus.pp.eventmate.ui.events.EventProgressState

class NewEventViewModel : BaseViewModel() {
	
	
	private val roomDatabase by lazy { App.COMPONENTS.provideDatabase() }
	
	private val _eventState: MutableLiveData<EventProgressState> = MutableLiveData()
	val eventProgressState: LiveData<EventProgressState> = _eventState
	
	fun createEvent(newEvent: Event) {
		roomDatabase.eventDao().insert(EventEntity(null, newEvent.name, newEvent.date))
	}
}