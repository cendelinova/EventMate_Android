package gr.tei.erasmus.pp.eventmate.helpers

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.list.customListAdapter
import gr.tei.erasmus.pp.eventmate.R
import kotlinx.android.synthetic.main.dialog_photo_preview.view.*
import kotlinx.android.synthetic.main.dialog_photo_preview.view.btn_close
import kotlinx.android.synthetic.main.dialog_video_preview.view.*
import kotlinx.android.synthetic.main.report_pick_dialog.view.*


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
		adapter: RecyclerView.Adapter<*>,
		customView: View,
		title: String,
		callback: View.OnClickListener?
	) {
		
		MaterialDialog(context).show {
			customView.my_title.text = title
			customView(view = customView, scrollable = true)
			customListAdapter(adapter)
			positiveButton(R.string.btn_confirm)
			negativeButton(R.string.btn_cancel)
			getActionButton(WhichButton.POSITIVE).setOnClickListener {
				callback?.onClick(it)
				this.dismiss()
			}
		}
	}
	
	fun showDialogWithPhotoPreview(
		context: Context,
		layoutInflater: LayoutInflater,
		uri: Uri? = null,
		photoString: String? = null
	) {
		val layout =
			layoutInflater.inflate(R.layout.dialog_photo_preview, null)
				.also {
					uri?.let { uri ->
						it.photo.setImageURI(uri)
					}
					
					photoString?.let { photoString ->
						it.photo.setImageBitmap(FileHelper.decodeImage(photoString))
					}
					
				}
		val dialog = AlertDialog.Builder(context).apply {
			setView(layout)
		}.create()
		
		with(dialog) {
			layout.btn_close.setOnClickListener {
				this.dismiss()
			}
			show()
		}
	}
	
	fun showDialogWithVideoPreview(
		context: Context,
		layoutInflater: LayoutInflater,
		uri: Uri? = null,
		data: String? = null
	) {
		val layout =
			layoutInflater.inflate(R.layout.dialog_photo_preview, null)
				.also {
					with(it.video_view) {
						//			setVideoURI(data)
						setMediaController(android.widget.MediaController(context).apply {
							setAnchorView(this)
						})
						requestFocus()
//			seekTo(1)
					}
//		toggleVideoButtons()
				}
		val dialog = AlertDialog.Builder(context).apply {
			setView(layout)
		}.create()
		
		with(dialog) {
			layout.btn_close.setOnClickListener {
				this.dismiss()
			}
			show()
		}
		
	}
	
}