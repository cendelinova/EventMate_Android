package gr.tei.erasmus.pp.eventmate.helpers

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.ui.report.ReportGuestAdapter
import kotlinx.android.synthetic.main.report_guest_dialog.view.*


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
	
	fun showCustomDialog(context: Context, customView: View, callback: DialogInterface.OnClickListener?) {
		AlertDialog.Builder(context).apply {
			setView(customView)
			setPositiveButton(R.string.btn_confirm, callback)
			setNegativeButton(
				R.string.btn_cancel
			) { dialog, _ -> dialog.dismiss() }
		}.also {
			it.create()
		}.show()
	}
	
	fun showDialogWithAdapter(
		context: Context,
		adapter: ReportGuestAdapter,
		customView: View,
		callback: DialogInterface.OnClickListener?
	) {
		
		with(customView.guest_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(customView.guest_empty_view)
			layoutManager = LinearLayoutManager(context).apply { orientation = RecyclerView.VERTICAL }
			this.adapter = adapter
		}
		
		AlertDialog.Builder(context).apply {
			setView(customView)
			setPositiveButton(R.string.btn_confirm, callback)
			setNegativeButton(
				R.string.btn_cancel
			) { dialog, _ -> dialog.dismiss() }
		}.also {
			it.create()
		}.show()
	}
}