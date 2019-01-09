package gr.tei.erasmus.pp.eventmate.ui.submission

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.AUDIO
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.PHOTO
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.VIDEO
import gr.tei.erasmus.pp.eventmate.data.model.SubmissionFile
import gr.tei.erasmus.pp.eventmate.helpers.DialogHelper
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper.getRealPathFromURI
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper
import gr.tei.erasmus.pp.eventmate.helpers.TextInputLayoutHelper
import gr.tei.erasmus.pp.eventmate.ui.base.*
import kotlinx.android.synthetic.main.activity_assignee_new_submission.*
import nl.changer.audiowife.AudioWife


class AssigneeNewSubmissionActivity : BaseActivity() {
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(SubmissionViewModel::class.java) }
	
	private lateinit var submissionType: SubmissionFile.FileType
	
	private var dataUri: Uri? = null
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_assignee_new_submission)
		
		setupToolbar(toolbar)
		observeViewModel()
		parseIntent()
		handleSendingNewSubmission()
	}
	
	private fun handleSendingNewSubmission() {
		btn_send_submission.setOnClickListener {
			val name = TextInputLayoutHelper.getDefaultTextIfEmpty(input_submission_name.editText?.text.toString())
			val comment =
				TextInputLayoutHelper.getDefaultTextIfEmpty(input_submission_comment.editText?.text.toString())
			
			val data = getData()
			if (data == null) {
				Snackbar.make(main, getString(R.string.error_uploading_data), Snackbar.LENGTH_SHORT).show()
			}
			
			data?.run {
				viewModel.saveNewSubmissionFile(8, SubmissionFile(name, comment, submissionType.name, this))
			}
			
		}
	}
	
	private fun getData(): String? {
		return when (submissionType) {
			SubmissionFile.FileType.PHOTO -> FileHelper.encodeImage(FileHelper.convertImageViewToBitmap(photo))
			SubmissionFile.FileType.VIDEO -> FileHelper.encodeFile(getRealPathFromURI(dataUri, contentResolver))
			SubmissionFile.FileType.AUDIO -> FileHelper.encodeFile(dataUri.toString())
		}
	}
	
	
	private fun observeViewModel() {
		with(viewModel) {
			observe(states, observeSubmissionState)
		}
	}
	
	private val observeSubmissionState = Observer<State> { state ->
		when (state) {
			is LoadingState -> StateHelper.toggleProgress(progress, true)
			is ErrorState -> StateHelper.showError(state.error, progress, main)
			is FinishedState -> StateHelper.toggleProgress(progress, false)
			is SubmissionViewModel.SubmissionState -> {
				StateHelper.toggleProgress(progress, false)
			}
		}
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
		submissionType = SubmissionFile.FileType.AUDIO
		val audioData = Uri.parse(intent.getStringExtra(AUDIO))
		dataUri = audioData
		
		photo.visibility = View.GONE
		video_layout.visibility = View.GONE
		
		AudioWife.getInstance().init(this, audioData)
			.useDefaultUi(audio_layout, layoutInflater)
	}
	
	
	private fun preparePhotoLayout() {
		submissionType = SubmissionFile.FileType.PHOTO
		val uri: Uri = Uri.parse(intent.getStringExtra(PHOTO))
		
		Picasso.get().load(uri).into(photo)
		
		video_layout.visibility = View.GONE
		audio_layout.visibility = View.GONE
		
		photo.setOnClickListener {
			DialogHelper.showDialogWithPhotoPreview(this, layoutInflater, uri)
		}
	}
	
	private fun prepareVideoLayout() {
		submissionType = SubmissionFile.FileType.VIDEO
		val data: Uri = Uri.parse(intent.getStringExtra(VIDEO))
		dataUri = data
		
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
