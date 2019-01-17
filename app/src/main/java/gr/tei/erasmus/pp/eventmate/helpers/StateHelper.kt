package gr.tei.erasmus.pp.eventmate.helpers

import android.view.View
import android.widget.ProgressBar
import com.google.android.material.button.MaterialButton
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
	
	fun toggleButton(eventOwner: User, state: Event.EventState, button: MaterialButton) {
		val userRoleHelper = App.COMPONENTS.provideUserRoleHelper()
		button.visibility = when (state) {
			EDITABLE -> {
				if (button.id == R.id.btn_add_guests && !userRoleHelper.isSameUser(eventOwner)) {
					View.GONE
				} else View.VISIBLE
			}
			READY_TO_PLAY, IN_PLAY, UNDER_EVALUATION, FINISHED, UNDEFINED_STATE -> View.GONE
		}
	}
	
	
	/**
	 * Toggle progress visibility
	 * @param progress progress widget
	 * @param visibility should be visible
	 */
	fun toggleProgress(progress: ProgressBar, visibility: Boolean) {
		progress.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
	}
	
	fun showError(error: Throwable, progress: ProgressBar, view: View) {
		Timber.e("Error occurred $error ")
		toggleProgress(progress, false)
		error.message?.let {
			Snackbar.make(
				view,
				it,
				Snackbar.LENGTH_LONG
			).show()
		}
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
				fab.setImageResource(FINISHED.iconFab)
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
				fab.visibility = View.GONE
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
			visibility = if (userRoleHelper.isSameUser(user)) View.VISIBLE else View.GONE
		}
	}
}