package gr.tei.erasmus.pp.eventmate.ui.submission

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.MediaController
import androidx.appcompat.app.AlertDialog
import com.squareup.picasso.Picasso
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.AUDIO
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.PHOTO
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.VIDEO
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_assignee_new_submission.*
import kotlinx.android.synthetic.main.dialog_photo_preview.view.*
import nl.changer.audiowife.AudioWife


class AssigneeNewSubmissionActivity : BaseActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_assignee_new_submission)
		
		setupToolbar(toolbar)
		
		parseIntent()
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
		if (intent.hasExtra(PHOTO) && intent.getStringExtra(PHOTO) != null) {
			preparePhotoLayout()
		} else if (intent.hasExtra(VIDEO) && intent.getStringExtra(VIDEO) != null) {
			prepareVideoLayout()
		} else if (intent.hasExtra(AUDIO) && intent.getStringExtra(AUDIO) != null) {
			prepareAudioLayout()
		}
	}
	
	private fun prepareAudioLayout() {
		val audioData = Uri.parse(intent.getStringExtra(AUDIO))
		photo.visibility = View.GONE
		video_layout.visibility = View.GONE
		
		AudioWife.getInstance().init(this, audioData)
			.useDefaultUi(audio_layout, layoutInflater)
	}
	
	
	private fun preparePhotoLayout() {
		val uri: Uri = Uri.parse(intent.getStringExtra(PHOTO))
		
		Picasso.get().load(uri).into(photo)
		
		video_layout.visibility = View.GONE
		audio_layout.visibility = View.GONE
		
		photo.setOnClickListener {
			val layout =
				LayoutInflater.from(this@AssigneeNewSubmissionActivity).inflate(R.layout.dialog_photo_preview, null)
					.also {
						it.photo.setImageURI(uri)
					}
			val dialog = AlertDialog.Builder(this).apply {
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
	
	private fun prepareVideoLayout() {
		val data: Uri = Uri.parse(intent.getStringExtra(VIDEO))
		
		photo.visibility = View.GONE
		audio_layout.visibility = View.GONE
		
		with(video_view) {
			setVideoURI(data)
			setMediaController(MediaController(this@AssigneeNewSubmissionActivity).apply {
				setAnchorView(video_view)
			})
			requestFocus()
			seekTo(1)
		}
		toggleVideoButtons()
	}
}
