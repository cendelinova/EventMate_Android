package gr.tei.erasmus.pp.eventmate.ui.submission

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.squareup.picasso.Picasso
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.AUDIO
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.PHOTO
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.VIDEO
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_assignee_new_submission.*
import kotlinx.android.synthetic.main.dialog_photo_preview.view.*


class AssigneeNewSubmissionActivity : BaseActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_assignee_new_submission)
		
		setupToolbar(toolbar)
		
		parseIntent()
		toggleVideoButtons()
		
	}
	
	private fun toggleVideoButtons() {
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
	
	private fun parseIntent() {
		if (intent.hasExtra(PHOTO) || intent.hasExtra(VIDEO) || intent.hasExtra(AUDIO)) {
//			val data: Uri = Uri.parse(intent.getStringExtra(VIDEO))
//
//			video_view.setVideoURI(data)
//			video_view.setMediaController(MediaController(this).apply {
//				setAnchorView(video_view)
//			})
//			video_view.requestFocus()
//			video_view.seekTo(1)
			
			val data1: Uri = Uri.parse(intent.getStringExtra(PHOTO))
			
			Picasso.get().load(data1).into(photo)
			
			photo.setOnClickListener {
				val layout =
					LayoutInflater.from(this@AssigneeNewSubmissionActivity).inflate(R.layout.dialog_photo_preview, null)
						.also {
							it.photo.setImageURI(data1)
						}
				val dialog = AlertDialog.Builder(this).apply {
					setView(layout)
				}.create()
				
				layout.btn_close.setOnClickListener {
					dialog.dismiss()
				}
				dialog.show()
			}
			
		}
	}
}
