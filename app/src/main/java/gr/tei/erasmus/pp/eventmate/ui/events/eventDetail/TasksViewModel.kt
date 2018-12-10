package gr.tei.erasmus.pp.eventmate.ui.events.eventDetail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.ui.base.BaseViewModel
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import gr.tei.erasmus.pp.eventmate.ui.events.EventsViewModel
import kotlinx.coroutines.launch

class TasksViewModel: BaseViewModel() {
	
	private val taskRepository by lazy { App.COMPONENTS.provideTaskRepository() }
	
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	private var allTasks = mutableListOf<Task>()
	
	
	fun getTasks() {
		launch {
			mStates.postValue(LoadingState)
			allTasks.clear()
			try {
				val tasks = taskRepository.getAllTasks().await()
				allTasks.addAll(tasks)
				mStates.postValue(TaskListState(tasks))
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