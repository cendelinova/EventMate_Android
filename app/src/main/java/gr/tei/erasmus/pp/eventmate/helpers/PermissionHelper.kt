package gr.tei.erasmus.pp.eventmate.helpers

import android.Manifest
import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity


object PermissionHelper {
	fun showPermissionDialog(activity: BaseActivity, context: Context, view: View) {
		val dialogMultiplePermissionsListener: MultiplePermissionsListener =
			DialogOnAnyDeniedMultiplePermissionsListener.Builder
				.withContext(context)
				.withTitle(R.string.camera_audio_permission_title)
				.withMessage(R.string.camera_audio_permission_msg)
				.withButtonText(android.R.string.ok)
				.withIcon(R.mipmap.ic_launcher)
				.build()
		
		val snackbarMultiplePermissionsListener: MultiplePermissionsListener =
			SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
				.with(view, R.string.needed_camera_permission)
				.withOpenSettingsButton(R.string.settings)
				.withCallback(object : Snackbar.Callback() {
					override fun onShown(snackbar: Snackbar?) {
						// Event handler for when the given Snackbar has been dismissed
					}
					
					override fun onDismissed(snackbar: Snackbar?, event: Int) {
						// Event handler for when the given Snackbar is visible
					}
				})
				.build()
		
		Dexter.withActivity(activity)
			.withPermissions(
				Manifest.permission.CAMERA,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.RECORD_AUDIO,
				Manifest.permission.READ_CONTACTS
			).withListener(
				CompositeMultiplePermissionsListener(
					snackbarMultiplePermissionsListener,
					dialogMultiplePermissionsListener
				)
			)
			.check()
		
		
	}
	
	
}