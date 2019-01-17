package gr.tei.erasmus.pp.eventmate.ui.report

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Email
import gr.tei.erasmus.pp.eventmate.data.model.ReportRequest
import gr.tei.erasmus.pp.eventmate.data.model.ReportResponse
import gr.tei.erasmus.pp.eventmate.helpers.ErrorHelper
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import gr.tei.erasmus.pp.eventmate.ui.base.*
import gr.tei.erasmus.pp.eventmate.ui.events.EventsViewModel
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.guests.UserViewModel
import gr.tei.erasmus.pp.eventmate.ui.events.eventDetail.tasks.TasksViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class ReportViewModel : BaseViewModel() {
	
	private val userRepository = App.COMPONENTS.provideUserRepository()
	private val taskRepository = App.COMPONENTS.provideTaskRepository()
	private val reportRepository = App.COMPONENTS.provideReportRepository()
	private val eventRepository = App.COMPONENTS.provideEventRepository()
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	fun getEventGuests(eventId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = userRepository.getGuests(eventId).await()
				Timber.d("getEventGuests() $response ${response.isSuccessful}")
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
	
	fun getEventTasks(eventId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = taskRepository.getEventTasks(eventId).await()
				Timber.d("getEventTasks() with id: $eventId $response ${response.isSuccessful}")
				val state = if (response.isSuccessful && response.body() != null) {
					TasksViewModel.TaskListState(response.body()!!)
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
	
	fun deleteReport(reportId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = reportRepository.deleteReport(reportId).await()
				Timber.d("deleteReport() with id: $reportId $response ${response.isSuccessful}")
				val state = if (response.isSuccessful && response.body() != null) {
					ReportState(response.body()!!)
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
	
	fun getEventReports(eventId: Long, showLoading: Boolean = true) {
		launch {
			if (showLoading) mStates.postValue(LoadingState)
			try {
				val response = reportRepository.getReports(eventId).await()
				Timber.d("getEventReports() with id: $eventId $response ${response.isSuccessful}")
				val state = if (response.isSuccessful && response.body() != null) {
					ReportState(response.body()!!)
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
	
	fun saveEventReport(eventId: Long, reportRequest: ReportRequest) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = reportRepository.saveReport(eventId, reportRequest).await()
				Timber.d("saveEventReport() with id: $eventId $response ${response.isSuccessful}")
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
	
	fun shareReport(reportId: Long, email: Email) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = reportRepository.shareReport(reportId, email).await()
				Timber.d("shareReport() with id: $reportId $response ${response.isSuccessful}")
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
	
	fun saveFileLocally(context: Context, data: String) {
		launch {
			mStates.postValue(LoadingState)
			FileHelper.saveFileLocally(
				context,
				data,
				".pdf",
				"application/pdf"
			)
			
			mStates.postValue(FinishedState)
		}
		
	}
	
	fun getEvent(eventId: Long, showProgress: Boolean = true) {
		launch {
			if (showProgress) mStates.postValue(LoadingState)
			try {
				val response = eventRepository.getEvent(eventId).await()
				val state = if (response.isSuccessful && response.body() != null) {
					EventsViewModel.EventListState(mutableListOf(response.body()!!))
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
	
	
	data class ReportState(val reports: MutableList<ReportResponse>) : State() {
		companion object {
			fun from(list: MutableList<ReportResponse>): ReportState {
				return if (list.isEmpty()) error("report list should not be empty")
				else {
					ReportState(list)
				}
			}
		}
	}
}