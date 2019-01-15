package gr.tei.erasmus.pp.eventmate.helpers

import android.view.View
import android.widget.ProgressBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.Event.EventState.*
import gr.tei.erasmus.pp.eventmate.data.model.Task
import gr.tei.erasmus.pp.eventmate.data.model.Task.TaskState.IN_REVIEW
import gr.tei.erasmus.pp.eventmate.data.model.User
import timber.log.Timber

object StateHelper {
	
	
	/**
	 * Toggle progress visibility
	 * @param progress progress widget
	 * @param visibility shoudl be visible
	 */
	fun toggleProgress(progress: ProgressBar, visibility: Boolean) {
		progress.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
	}
	
	fun showError(error: Throwable, progress: ProgressBar, view: View, errorMessage: Int = R.string.loading_error) {
		val context = App.COMPONENTS.provideContext()
		Timber.e("Error $error while fetching")
		toggleProgress(progress, false)
		Snackbar.make(
			view,
			context.getString(errorMessage),
			Snackbar.LENGTH_LONG
		).show()
	}
	
	fun prepareEventFab(event: Event, fab: FloatingActionButton, fabListener: View.OnClickListener) {
		val userRoleHelper = App.COMPONENTS.provideUserRoleHelper()
		
		val state = Event.EventState.valueOf(event.state)
		when (state) {
			Event.EventState.EDITABLE -> {
				event.eventOwner?.let {
					setFabSettings(
						fab, Event.EventState.EDITABLE.iconFab, fabListener,
						it, userRoleHelper
					)
				}
			}
			READY_TO_PLAY -> {
				event.eventOwner?.let {
					setFabSettings(
						fab, READY_TO_PLAY.iconFab, fabListener,
						it, userRoleHelper
					)
				}
			}
			Event.EventState.IN_PLAY -> {
				event.eventOwner?.let {
					setFabSettings(
						fab, Event.EventState.IN_PLAY.iconFab, fabListener,
						it, userRoleHelper
					)
				}
			}
			UNDER_EVALUATION -> {
				event.eventOwner?.let {
					setFabSettings(
						fab, UNDER_EVALUATION.iconFab, fabListener,
						it, userRoleHelper
					)
				}
			}
			
			Event.EventState.FINISHED -> {
				event.eventOwner?.let {
					setFabSettings(
						fab, Event.EventState.FINISHED.iconFab, fabListener,
						it, userRoleHelper
					)
				}
			}
			
			UNDEFINED_STATE -> {
				return
			}
		}
	}
	
	fun prepareTaskFab(task: Task, fab: FloatingActionButton, fabListener: View.OnClickListener) {
		val userRoleHelper = App.COMPONENTS.provideUserRoleHelper()
		
		val state = Task.TaskState.valueOf(task.taskState)
		when (state) {
			Task.TaskState.EDITABLE -> {
				setFabSettings(
					fab, Task.TaskState.EDITABLE.fabIcon, fabListener,
					task.taskOwner, userRoleHelper
				)
				
			}
			Task.TaskState.READY_TO_START -> {
				setFabSettings(
					fab, READY_TO_PLAY.iconFab, fabListener,
					task.taskOwner, userRoleHelper
				)
				
			}
			Task.TaskState.IN_PLAY -> {
				setFabSettings(
					fab, Task.TaskState.IN_PLAY.fabIcon, fabListener,
					task.taskOwner, userRoleHelper
				)
			}
			IN_REVIEW -> {
				
				setFabSettings(
					fab, UNDER_EVALUATION.iconFab, fabListener,
					task.taskOwner, userRoleHelper
				)
			}
			
			Task.TaskState.FINISHED -> {
				setFabSettings(
					fab, Task.TaskState.FINISHED.fabIcon, fabListener,
					task.taskOwner, userRoleHelper
				)
			}
			
		}
		
	}
	
	private fun setFabSettings(
		fab: FloatingActionButton,
		imageResource: Int,
		listener: View.OnClickListener?,
		user: User,
		userRoleHelper: UserRoleHelper
	) {
		with(fab) {
			setImageResource(imageResource)
			setOnClickListener(listener)
			// todo more roles
//			visibility = if (userRoleHelper.isEventOwner(user)) View.VISIBLE else View.GONE
		}
	}
}