package gr.tei.erasmus.pp.eventmate.ui.tasks

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.ui.base.*
import kotlinx.coroutines.launch

class NewTaskViewModel : BaseViewModel() {
	
	private val taskRepository by lazy { App.COMPONENTS.provideTaskRepository() }
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	fun createTask(task: Task) {
		launch {
			mStates.postValue(LoadingState)
			try {
				taskRepository.insert(Task.convertToEntity(task))
				mStates.postValue(FinishedState)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
}