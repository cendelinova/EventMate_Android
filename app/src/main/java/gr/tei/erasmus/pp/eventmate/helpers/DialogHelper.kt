package gr.tei.erasmus.pp.eventmate.helpers

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.list.customListAdapter
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.data.model.ReportInfoDTO
import kotlinx.android.synthetic.main.dialog_audio_preview.view.*
import kotlinx.android.synthetic.main.dialog_photo_preview.view.*
import kotlinx.android.synthetic.main.dialog_video_preview.view.*
import kotlinx.android.synthetic.main.report_event_info_dialog.view.*
import kotlinx.android.synthetic.main.report_pick_dialog.view.*
import nl.changer.audiowife.AudioWife
import kotlinx.android.synthetic.main.dialog_audio_preview.view.btn_close as close_audio
import kotlinx.android.synthetic.main.dialog_audio_preview.view.progress as progress_audio
import kotlinx.android.synthetic.main.dialog_photo_preview.view.btn_close as close_photo
import kotlinx.android.synthetic.main.dialog_video_preview.view.btn_close as close_video
import kotlinx.android.synthetic.main.dialog_video_preview.view.progress as progress_video
import kotlinx.android.synthetic.main.report_pick_dialog.view.my_title as title


object DialogHelper {
	fun showDeleteDialog(context: Context, deleteCallback: DialogInterface.OnClickListener) {
		AlertDialog.Builder(context).apply {
			setPositiveButton(R.string.btn_confirm, deleteCallback)
			setNegativeButton(
				R.string.btn_cancel
			) { dialog, _ -> dialog.dismiss() }
			setMessage(R.string.message_wish_delete)
			setTitle(R.string.title_delete)
		}.also {
			it.create()
		}.show()
	}
	
	fun showEventReportDialog(
		context: Context,
		layoutInflater: LayoutInflater,
		reportInfoDTO: ReportInfoDTO,
		callback: DialogInterface.OnClickListener?
	) {
		AlertDialog.Builder(context).apply {
			val layout = layoutInflater.inflate(R.layout.report_event_info_dialog, null)
			with(reportInfoDTO) {
				layout.event_name.isChecked = includeName
				layout.event_date.isChecked = includeDate
				layout.event_owner.isChecked = includeOwner
				layout.event_place.isChecked = includePlace
				layout.report_creator.isChecked = includeReportCreator
				layout.report_created_date.isChecked = includeReportCreatedDate
			}
			setView(layout)
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
		confirmListener: View.OnClickListener?,
		queryListener: SearchView.OnQueryTextListener?
	) {
		
		MaterialDialog(context).show {
			if (!title.isEmpty() && customView.my_title != null) {
				customView.my_title.text = title
			}
			customView.search_view.setOnQueryTextListener(queryListener)
			customView(view = customView, scrollable = true)
			customListAdapter(adapter)
			positiveButton(R.string.btn_confirm)
			negativeButton(R.string.btn_cancel)
			getActionButton(WhichButton.POSITIVE).setOnClickListener {
				confirmListener?.onClick(it)
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
			layout.close_photo.setOnClickListener {
				this.dismiss()
			}
			show()
		}
	}
	
	fun showDialogWithVideoPreview(
		context: Context,
		layoutInflater: LayoutInflater,
		data: String? = null
	) {
		val layout =
			layoutInflater.inflate(R.layout.dialog_video_preview, null)
				.also {
					StateHelper.toggleProgress(it.progress_audio, true)
					with(it.video_view) {
						setVideoURI(FileHelper.decodeFile(context, data))
						setMediaController(android.widget.MediaController(context).apply {
							setAnchorView(this)
						})
						requestFocus()
						seekTo(1)
					}
					toggleVideoButtons(it)
					StateHelper.toggleProgress(it.progress_audio, false)
					
				}
		val dialog = AlertDialog.Builder(context).apply {
			setView(layout)
		}.create()
		
		with(dialog) {
			layout.close_video.setOnClickListener {
				this.dismiss()
			}
			show()
		}
	}
	
	fun showDialogWithAudioPlayer(
		context: Context,
		layoutInflater: LayoutInflater,
		data: String? = null
	) {
		val layout =
			layoutInflater.inflate(R.layout.dialog_audio_preview, null)
				.also {
					StateHelper.toggleProgress(it.progress_audio, true)
					AudioWife.getInstance().init(context, FileHelper.decodeFile(context, data))
						.useDefaultUi(it.audio_layout, layoutInflater)
					StateHelper.toggleProgress(it.progress_audio, false)
				}
		val dialog = AlertDialog.Builder(context).apply {
			setView(layout)
		}.create()
		
		with(dialog) {
			layout.close_audio.setOnClickListener {
				this.dismiss()
			}
			show()
		}
	}
	
	private fun toggleVideoButtons(view: View) {
		with(view) {
			btn_play.setOnClickListener {
				if (!video_view.isPlaying) {
					video_view.start()
					btn_play.visibility = View.GONE
				}
			}
			video_view.setOnCompletionListener {
				btn_play.visibility = View.VISIBLE
			}
		}
	}
	
}