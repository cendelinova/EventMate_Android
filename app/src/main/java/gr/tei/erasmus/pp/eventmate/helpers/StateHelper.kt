package gr.tei.erasmus.pp.eventmate.helpers

import android.content.res.Resources
import android.view.View
import android.widget.ProgressBar
import com.google.android.material.snackbar.Snackbar
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.app.App
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
	
	fun showError(error: Throwable, progress: ProgressBar, view: View) {
		val context = App.COMPONENTS.provideContext()
		
		Timber.e("Error $error while fetching")
		toggleProgress(progress, false)
		Snackbar.make(
			view,
			context.getString(R.string.loading_error),
			Snackbar.LENGTH_INDEFINITE
		).show()
		
	}
}