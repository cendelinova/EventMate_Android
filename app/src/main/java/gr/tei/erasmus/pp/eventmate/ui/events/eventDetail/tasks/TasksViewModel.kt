package gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.ui.base.BaseViewModel
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import kotlinx.coroutines.launch

class TasksViewModel : BaseViewModel() {
	
	private val taskRepository by lazy { App.COMPONENTS.provideTaskRepository() }
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	fun getTasks(eventId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = taskRepository.getEventTasks(eventId).await()
				val state = if (response.isSuccessful && response.body() != null) {
					TaskListState(response.body()!!)
				} else {
					ErrorState(Throwable("Error"))
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
					TaskListState(mutableListOf(response.body()!!))
				} else {
					ErrorState(Throwable("Error"))
				}
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun deleteTask(taskId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = taskRepository.deleteTask(taskId).await()
				val state = if (response.isSuccessful && response.body() != null) {
					TaskListState(response.body()!!)
				} else {
					ErrorState(Throwable("Error"))
				}
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	data class TaskListState(val tasks: MutableList<Task>) : State() {
		companion object {
			fun from(list: MutableList<Task>): TaskListState {
				return if (list.isEmpty()) error("event list should not be empty")
				else {
					TaskListState(list)
				}
			}
		}
	}
}