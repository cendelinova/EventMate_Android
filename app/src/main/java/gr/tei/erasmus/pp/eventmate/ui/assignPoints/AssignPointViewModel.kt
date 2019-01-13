package gr.tei.erasmus.pp.eventmate.ui.assignPoints

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.UserSubmissionPoints
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.tasks.TasksViewModel
import kotlinx.coroutines.launch

class AssignPointViewModel: BaseViewModel() {
	
	private val taskRepository = App.COMPONENTS.provideTaskRepository()
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	fun getTask(taskId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = taskRepository.getTask(taskId).await()
				val state = if (response.isSuccessful && response.body() != null) {
					TasksViewModel.TaskListState(mutableListOf(response.body()!!))
				} else {
					ErrorState(Throwable("Error"))
				}
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun assignPoints(taskId: Long, userSubmissionPointList: MutableList<UserSubmissionPoints>) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = taskRepository.assignPoints(taskId, userSubmissionPointList).await()
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
}
