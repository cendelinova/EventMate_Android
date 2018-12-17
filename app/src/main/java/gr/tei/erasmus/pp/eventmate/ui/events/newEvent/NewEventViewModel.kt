package gr.tei.erasmus.pp.eventmate.ui.events.newEvent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.EventRequest
import gr.tei.erasmus.pp.eventmate.ui.base.*
import kotlinx.coroutines.launch

class NewEventViewModel : BaseViewModel() {
	
	private val eventRepository by lazy { App.COMPONENTS.provideEventRepository() }
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	
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
}