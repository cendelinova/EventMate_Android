package gr.tei.erasmus.pp.eventmate.ui.submission

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder
import cafe.adriel.androidaudiorecorder.model.AudioChannel
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate
import cafe.adriel.androidaudiorecorder.model.AudioSource
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.AUDIO
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.PHOTO
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.SUBMISSION_EXTRA
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.VIDEO
import gr.tei.erasmus.pp.eventmate.data.model.SubmissionExtra
import gr.tei.erasmus.pp.eventmate.data.model.SubmissionFile
import gr.tei.erasmus.pp.eventmate.data.model.SubmissionResponse
import gr.tei.erasmus.pp.eventmate.helpers.DialogHelper
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import gr.tei.erasmus.pp.eventmate.helpers.StateHelper
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import gr.tei.erasmus.pp.eventmate.ui.base.ErrorState
import gr.tei.erasmus.pp.eventmate.ui.base.LoadingState
import gr.tei.erasmus.pp.eventmate.ui.base.State
import kotlinx.android.synthetic.main.activity_assignee_submission_list.*
import kotlinx.android.synthetic.main.submission_item.view.*
import kotlinx.android.synthetic.main.toolbar_task_detail.*
import timber.log.Timber


class AssigneeSubmissionListActivity : BaseActivity(), IPickResult {
	
	private val viewModel by lazy { ViewModelProviders.of(this).get(SubmissionViewModel::class.java) }
	
	private lateinit var submissionAdapter: SubmissionAdapter
	
	companion object {
		const val REQUEST_VIDEO_CAPTURE = 1
		const val REQUEST_PHOTO_CAPTURE = 2
		const val REQUEST_AUDIO_RECORD = 3
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_assignee_submission_list)
		
		initializeRecyclerView()
		
		setupToolbar(toolbar)
		
		observeViewModel()
		
		parseIntent()
		
		fab.addOnMenuItemClickListener { _, _, id ->
			when (id) {
				R.id.action_take_photo -> dispatchTakePhotoIntent()
				R.id.action_record_video -> dispatchTakeVideoIntent()
				R.id.action_record_audio -> {
					findAudioRecord()
					openAudioRecord()
				}
			}
		}
		
	}
	
	override fun onPickResult(pickResult: PickResult?) {
		pickResult?.let {
			val intent = Intent(this, AssigneeNewSubmissionActivity::class.java)
			intent.putExtra(PHOTO, pickResult.uri.toString())
			startActivity(intent)
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
			is SubmissionViewModel.SubmissionState -> {
				StateHelper.toggleProgress(progress, false)
				setupLayout(state.submissionResponses[0])
			}
		}
	}
	
	private fun setupLayout(submissionResponse: SubmissionResponse) {
		with(submissionResponse) {
			task_name.text = taskName
			taskPhoto?.let {
				task_photo.setImageBitmap(FileHelper.decodeImage(it))
			}
			submissionAdapter.updateSubmissionList(content.sortedBy { it.created?.time?.compareTo(it.created.time) }.toMutableList())
			
			taskDescription?.let {
				tv_description.text = taskDescription
			}
			
			
			SpannableStringBuilder().also {
				val startString = getString(R.string.assignee_submission)
				SpannableString(startString + " " + submitter?.userName).apply {
					
					setSpan(
						ForegroundColorSpan(getColor(R.color.colorAccent)),
						startString.length + 1,
						startString.length + 1 + submitter.userName.length,
						0
					)
					
					setSpan(
						StyleSpan(Typeface.BOLD),
						startString.length + 1,
						startString.length + 1 + submitter.userName.length,
						0
					)
					
					it.append(this)
					tasks_status.setText(it, TextView.BufferType.SPANNABLE)
				}
			}
			
			task_status_icon.visibility = View.GONE
			
		}
	}
	
	private fun parseIntent() {
		if (intent.hasExtra(SUBMISSION_EXTRA) && intent.getParcelableExtra<SubmissionExtra>(SUBMISSION_EXTRA) != null) {
			val data = intent.getParcelableExtra<SubmissionExtra>(SUBMISSION_EXTRA)
			// todo real data
			viewModel.getUserTaskSubmissions(data.userId, 8)
		}
	}
	
	private fun dispatchTakePhotoIntent() {
		PickImageDialog.build(PickSetup().apply {
			setTitle(R.string.choose_photo)
		}).show(this)
	}
	
	private fun dispatchTakeVideoIntent() {
		Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
			takeVideoIntent.resolveActivity(packageManager)?.also {
				startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
			}
		}
	}
	
	private val mSampleRates = intArrayOf(8000, 11025, 22050, 44100)
	
	private fun findAudioRecord(): AudioRecord? {
		for (rate in mSampleRates) {
			for (audioFormat in intArrayOf(AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT)) {
				for (channelConfig in intArrayOf(AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO)) {
					try {
						Timber.d(
							"Attempting rate $rate Hz, bits: $audioFormat channel: $channelConfig"
						)
						val bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig.toInt(), audioFormat.toInt())
						
						if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
							// check if we can instantiate and have a success
							val recorder = AudioRecord(
								MediaRecorder.AudioSource.DEFAULT,
								rate,
								channelConfig,
								audioFormat,
								bufferSize
							)
							
							if (recorder.state == AudioRecord.STATE_INITIALIZED)
								return recorder
						}
					} catch (e: Exception) {
						Timber.d("$rate Exception, keep trying.")
					}
					
				}
			}
		}
		return null
	}
	
	/**
	 * We have blacklist items obtained, initialize recyclerView and display them.
	 */
	private fun initializeRecyclerView() {
		Timber.v("initializeRecyclerView() called")
		
		submissionAdapter = SubmissionAdapter(this, submissionListener, mutableListOf())
		
		with(submission_recycler_view) {
			setHasFixedSize(true)
			setEmptyView(submission_empty_view)
			layoutManager = LinearLayoutManager(context)
			adapter = submissionAdapter
		}
	}
	
	private val submissionListener = object : SubmissionAdapter.SubmissionListener {
		override fun onSubmissionClick(itemView: View, slideIn: Boolean) {
			if (slideIn) {
				YoYo.with(Techniques.SlideInLeft)
					.duration(700)
					.repeat(0)
					.onStart {
						itemView.view_background.bringToFront()
					}
					.playOn(itemView.view_background)
				
			} else {
				YoYo.with(Techniques.SlideOutRight)
					.duration(700)
					.repeat(0)
					.onEnd {
						itemView.view_foreground.bringToFront()
					}
					.playOn(itemView.view_background)
			}
		}
		
		override fun onSubmissionView(submissionFile: SubmissionFile) {
			when (SubmissionFile.FileType.valueOf(submissionFile.type)) {
				SubmissionFile.FileType.PHOTO -> DialogHelper.showDialogWithPhotoPreview(
					this@AssigneeSubmissionListActivity,
					layoutInflater,
					null,
					submissionFile.data
				)
				
				SubmissionFile.FileType.VIDEO -> {
					DialogHelper.showDialogWithVideoPreview(
						this@AssigneeSubmissionListActivity,
						layoutInflater,
						null,
						submissionFile.data
					)
				}
				
				SubmissionFile.FileType.AUDIO -> {
					// todo audio
				}
			}
		}
		
		override fun onSubmissionDownload(submissionFile: SubmissionFile) {
		}
		
		override fun onSubmissionDelete(submissionFile: SubmissionFile) {
		}
		
	}
	
	private fun openAudioRecord() {
		val filePath = Environment.getExternalStorageDirectory().toString() + "/recorded_audio.wav"
		AndroidAudioRecorder.with(this)
			.setFilePath(filePath)
			.setColor(ContextCompat.getColor(this, R.color.colorPrimary))
			.setRequestCode(REQUEST_AUDIO_RECORD)
			.setSource(AudioSource.MIC)
			.setChannel(AudioChannel.STEREO)
			.setSampleRate(AudioSampleRate.HZ_48000)
			.setAutoStart(false)
			.setKeepDisplayOn(true)
			.record()
	}
	
	
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		
		if (resultCode == Activity.RESULT_OK) {
			val intent = Intent(this, AssigneeNewSubmissionActivity::class.java)
			when (requestCode) {
				REQUEST_AUDIO_RECORD -> intent.putExtra(AUDIO, data?.data.toString())
				REQUEST_VIDEO_CAPTURE -> intent.putExtra(VIDEO, data?.data.toString())
			}
			
			startActivity(intent)
		}
	}
	
	
}
