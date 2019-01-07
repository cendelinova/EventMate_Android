package gr.tei.erasmus.pp.eventmate.ui.submission

import android.app.Activity
import android.content.Intent
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder
//import cafe.adriel.androidaudiorecorder.model.AudioChannel
//import cafe.adriel.androidaudiorecorder.model.AudioSampleRate
//import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder
import cafe.adriel.androidaudiorecorder.AudioRecorderActivity
import cafe.adriel.androidaudiorecorder.model.AudioChannel
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate
import cafe.adriel.androidaudiorecorder.model.AudioSource
import com.squareup.picasso.Picasso
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.AUDIO
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.PHOTO
import gr.tei.erasmus.pp.eventmate.constants.Constants.Companion.VIDEO
import gr.tei.erasmus.pp.eventmate.data.model.SubmissionFile
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_assignee_submission_list.*
import kotlinx.android.synthetic.main.activity_new_task.*
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.util.*


class AssigneeSubmissionListActivity : AppCompatActivity(), IPickResult {
	
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
	
	override fun onPickResult(pickResult: PickResult?) {
		pickResult?.let {
			val intent = Intent(this, AssigneeNewSubmissionActivity::class.java)
			intent.putExtra(PHOTO, pickResult.uri.toString())
			startActivity(intent)
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
			layoutManager = LinearLayoutManager(context)
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
