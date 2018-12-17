package gr.tei.erasmus.pp.eventmate.ui.newTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.TaskRequest
import gr.tei.erasmus.pp.eventmate.ui.base.*
import kotlinx.coroutines.launch

class NewTaskViewModel : BaseViewModel() {
	
	private val taskRepository by lazy { App.COMPONENTS.provideTaskRepository() }
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	fun createTask(task: TaskRequest) {
		launch {
			mStates.postValue(LoadingState)
			try {
				taskRepository.insert(task)
				mStates.postValue(FinishedState)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
}