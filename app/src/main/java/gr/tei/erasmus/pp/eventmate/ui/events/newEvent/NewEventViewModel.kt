package gr.tei.erasmus.pp.eventmate.ui.events.newEvent

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.ui.base.*
import kotlinx.coroutines.launch

class NewEventViewModel : BaseViewModel() {
	
	private val eventRepository by lazy { App.COMPONENTS.provideEventRepository() }
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	
	fun createEvent(newEvent: Event) {
		launch {
			mStates.postValue(LoadingState)
			try {
				eventRepository.insert(Event.convertToEntity(newEvent))
				mStates.postValue(FinishedState)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
}