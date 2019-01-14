package gr.tei.erasmus.pp.eventmate.helpers

import android.view.View
import android.widget.ProgressBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.Event
import gr.tei.erasmus.pp.eventmate.data.model.Event.EventState.*
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
			EDITABLE -> {
				event.eventOwner?.let {
					setFabSettings(
						fab, EDITABLE.iconFab, fabListener,
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
			IN_PLAY -> {
				event.eventOwner?.let {
					setFabSettings(
						fab, IN_PLAY.iconFab, fabListener,
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
			
			FINISHED -> {
				event.eventOwner?.let {
					setFabSettings(
						fab, FINISHED.iconFab, fabListener,
						it, userRoleHelper
					)
				}
			}
			
			UNDEFINED_STATE -> {
				return
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
			visibility = if (userRoleHelper.isEventOwner(user)) View.VISIBLE else View.GONE
		}
	}
}