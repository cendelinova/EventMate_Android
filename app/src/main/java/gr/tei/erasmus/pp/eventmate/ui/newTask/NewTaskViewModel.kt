package gr.tei.erasmus.pp.eventmate.ui.newTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.TaskRequest
import gr.tei.erasmus.pp.eventmate.helpers.ErrorHelper
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.tasks.TasksViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class NewTaskViewModel : BaseViewModel() {
	
	private val taskRepository = App.COMPONENTS.provideTaskRepository()
	
	private val userRepository = App.COMPONENTS.provideUserRepository()
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	fun createTask(task: TaskRequest) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = taskRepository.insert(task).await()
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
	
	fun getEventGuests(eventId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = userRepository.getGuests(eventId).await()
				val state = if (response.isSuccessful && response.body() != null) {
					UserViewModel.UserListState(response.body()!!)
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
	
	fun getTask(taskId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = taskRepository.getTask(taskId).await()
				val state = if (response.isSuccessful && response.body() != null) {
					TasksViewModel.TaskListState(mutableListOf(response.body()!!))
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
}