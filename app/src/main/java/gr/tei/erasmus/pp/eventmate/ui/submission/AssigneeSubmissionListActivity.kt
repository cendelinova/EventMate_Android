package gr.tei.erasmus.pp.eventmate.ui.submission

import android.app.Activity
import android.content.Intent
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder
import cafe.adriel.androidaudiorecorder.model.AudioChannel
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate
import cafe.adriel.androidaudiorecorder.model.AudioSource
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.AUDIO
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.PHOTO
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.VIDEO
import gr.tei.erasmus.pp.eventmate.data.model.SubmissionFile
import kotlinx.android.synthetic.main.activity_assignee_submission_list.*
import timber.log.Timber


class AssigneeSubmissionListActivity : AppCompatActivity() {
	
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
	
	private fun dispatchTakePhotoIntent() {
		Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePhotoIntent ->
			takePhotoIntent.resolveActivity(packageManager)?.also {
				startActivityForResult(takePhotoIntent, REQUEST_PHOTO_CAPTURE)
			}
		}
	}
	
	private fun dispatchTakeVideoIntent() {
		Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
			takeVideoIntent.resolveActivity(packageManager)?.also {
				startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
			}
		}
	}

//	override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
//		if (requestCode == Companion.REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
//			val videoUri: Uri = intent!!.data!!
//			camera.setVideoURI(videoUri)
//		}
//	}
	
	
	override fun onStart() {
		super.onStart()
	}
	
	private val mSampleRates = intArrayOf(8000, 11025, 22050, 44100)
	
	fun findAudioRecord(): AudioRecord? {
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
	
	
	override fun onResume() {
		super.onResume()
	}
	
	
	override fun onPause() {
		super.onPause()
	}
	
	override fun onStop() {
		super.onStop()
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
			layoutManager = LinearLayoutManager(context!!)
			adapter = submissionAdapter
		}
	}
	
	private val submissionListener = object : SubmissionAdapter.SubmissionListener {
		override fun onReportShare(submissionFile: SubmissionFile) {
		
		}
		
		override fun onReportDownload(submissionFile: SubmissionFile) {
		}
		
		override fun onReportDelete(submissionFile: SubmissionFile) {
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
			.setAutoStart(true)
			.setKeepDisplayOn(true)
			.record()
	}
	
	
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		
		if (resultCode == Activity.RESULT_OK) {
			val intent = Intent(this, AssigneeNewSubmissionActivity::class.java)
			when (requestCode) {
				REQUEST_PHOTO_CAPTURE -> intent.putExtra(PHOTO, data?.extras)
				REQUEST_AUDIO_RECORD -> intent.putExtra(AUDIO, data)
				REQUEST_VIDEO_CAPTURE -> intent.putExtra(VIDEO, data?.data.toString())
			}
			
			startActivity(intent)
		}
	}
	
	
}
