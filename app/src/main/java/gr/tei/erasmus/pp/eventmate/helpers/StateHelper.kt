package gr.tei.erasmus.pp.eventmate.helpers

import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.widget.ProgressBar
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
		Timber.e("Error $error while fetching")
		toggleProgress(progress, false)
		Snackbar.make(
			view,
			"balala",
//			Resources.getSystem().getString(R.string.loading_error),
			Snackbar.LENGTH_INDEFINITE
		).show()
		
	}
}