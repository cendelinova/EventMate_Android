package gr.tei.erasmus.pp.eventmate.helpers

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import gr.tei.erasmus.pp.eventmate.R

object DialogHelper {
	fun showDeleteDialog(context: Context, deleteCallback: DialogInterface.OnClickListener) {
		AlertDialog.Builder(context).apply {
			setPositiveButton(R.string.btn_confirm, deleteCallback)
			setNegativeButton(
				R.string.btn_cancel
			) { dialog, _ -> dialog.dismiss() }
			setMessage(R.string.message_wish_delete_event)
			setTitle(R.string.title_delete_event)
		}.also {
			it.create()
		}.show()
	}
}