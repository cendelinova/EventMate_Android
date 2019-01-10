package gr.tei.erasmus.pp.eventmate.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Report
import gr.tei.erasmus.pp.eventmate.ui.base.BaseViewModel
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.tasks.TasksViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class ReportViewModel : BaseViewModel() {
	
	private val userRepository = App.COMPONENTS.provideUserRepository()
	private val taskRepository = App.COMPONENTS.provideTaskRepository()
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	fun getEventGuests(eventId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = userRepository.getGuests(eventId).await()
				Timber.d("getEventGuests() $response ${response.isSuccessful}")
				if (response.isSuccessful && response.body() != null) {
					mStates.postValue(UserViewModel.UserListState(response.body()!!))
				}
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun getEventTasks(eventId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = taskRepository.getEventTasks(eventId).await()
				Timber.d("getEventTasks() with id: $eventId $response ${response.isSuccessful}")
				if (response.isSuccessful && response.body() != null) {
					mStates.postValue(TasksViewModel.TaskListState(response.body()!!))
				}
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	
	data class ReportState(val submissionResponses: MutableList<Report>) : State() {
		companion object {
			fun from(list: MutableList<Report>): ReportState {
				return if (list.isEmpty()) error("report list should not be empty")
				else {
					ReportState(list)
				}
			}
		}
	}
}