package gr.tei.erasmus.pp.eventmate.ui.submission

import android.app.Activity
import android.content.pm.ActivityInfo
import android.hardware.Camera
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.view.*
import gr.tei.erasmus.pp.eventmate.R
import timber.log.Timber
import java.io.File
import java.io.IOException


class VideoCapture : Activity(), View.OnClickListener, SurfaceHolder.Callback {
	override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
		if (!recording && usecamera) {
			if (previewRunning) {
				camera!!.stopPreview()
			}
			
			try {
				val p = camera!!.getParameters()
				
				p.setPreviewSize(camcorderProfile!!.videoFrameWidth, camcorderProfile!!.videoFrameHeight)
				p.setPreviewFrameRate(camcorderProfile!!.videoFrameRate)
				
				camera!!.setParameters(p)
				
				camera!!.setPreviewDisplay(holder)
				camera!!.startPreview()
				previewRunning = true
			} catch (e: IOException) {
				Timber.e(e)
				e.printStackTrace()
			}
			
			prepareRecorder()
		}
	}
	
	override fun surfaceDestroyed(holder: SurfaceHolder?) {
		Timber.d("surfaceDestroyed")
		if (recording) {
			recorder!!.stop()
			recording = false
		}
		recorder!!.release()
		if (usecamera) {
			previewRunning = false
			//camera.lock();
			camera!!.release()
		}
		finish()
	}
	
	val LOGTAG = "VIDEOCAPTURE"
	
	private var recorder: MediaRecorder? = null
	private var holder: SurfaceHolder? = null
	private var camcorderProfile: CamcorderProfile? = null
	private var camera: Camera? = null
	
	var recording = false
	var usecamera = true
	var previewRunning = false
	
	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		requestWindowFeature(Window.FEATURE_NO_TITLE)
		window.setFlags(
			WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN
		)
		requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
		
		camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW)
		
		setContentView(R.layout.video_capture)
		
		val cameraView = findViewById(R.id.camera) as SurfaceView
		holder = cameraView.holder
		holder!!.addCallback(this)
		holder!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
		
		cameraView.isClickable = true
		cameraView.setOnClickListener(this)
	}
	
	private fun prepareRecorder() {
		recorder = MediaRecorder()
		recorder!!.setPreviewDisplay(holder!!.surface)
		
		if (usecamera) {
			camera!!.unlock()
			recorder!!.setCamera(camera)
		}
		
		recorder!!.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
		recorder!!.setVideoSource(MediaRecorder.VideoSource.DEFAULT)
		
		recorder!!.setProfile(camcorderProfile)
		
		// This is all very sloppy
		if (camcorderProfile!!.fileFormat == MediaRecorder.OutputFormat.THREE_GPP) {
			try {
				val newFile = File.createTempFile("videocapture", ".3gp", Environment.getExternalStorageDirectory())
				recorder!!.setOutputFile(newFile.getAbsolutePath())
			} catch (e: IOException) {
				Timber.v("Couldn't create file")
				e.printStackTrace()
				finish()
			}
			
		} else if (camcorderProfile!!.fileFormat == MediaRecorder.OutputFormat.MPEG_4) {
			try {
				val newFile = File.createTempFile("videocapture", ".mp4", Environment.getExternalStorageDirectory())
				recorder!!.setOutputFile(newFile.getAbsolutePath())
			} catch (e: IOException) {
				Timber.v("Couldn't create file")
				e.printStackTrace()
				finish()
			}
			
		} else {
			try {
				val newFile = File.createTempFile("videocapture", ".mp4", Environment.getExternalStorageDirectory())
				recorder!!.setOutputFile(newFile.getAbsolutePath())
			} catch (e: IOException) {
				Timber.v("Couldn't create file")
				e.printStackTrace()
				finish()
			}
			
		}
		//recorder.setMaxDuration(50000); // 50 seconds
		//recorder.setMaxFileSize(5000000); // Approximately 5 megabytes
		
		try {
			recorder!!.prepare()
		} catch (e: IllegalStateException) {
			e.printStackTrace()
			finish()
		} catch (e: IOException) {
			e.printStackTrace()
			finish()
		}
		
	}
	
	override fun onClick(v: View) {
		if (recording) {
			recorder!!.stop()
			if (usecamera) {
				try {
					camera!!.reconnect()
				} catch (e: IOException) {
					e.printStackTrace()
				}
				
			}
			// recorder.release();
			recording = false
			Timber.v("Recording Stopped")
			// Let's prepareRecorder so we can record again
			prepareRecorder()
		} else {
			recording = true
			recorder!!.start()
			Timber.v("Recording Started")
		}
	}
	
	override fun surfaceCreated(holder: SurfaceHolder) {
		Timber.v("surfaceCreated")
		
		camera = Camera.open()
		
		try {
			camera!!.setPreviewDisplay(holder)
			camera!!.startPreview()
			previewRunning = true
		} catch (e: IOException) {
			Timber.e(e)
			e.printStackTrace()
		}
		
	}
	
	
}