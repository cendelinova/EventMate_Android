package gr.tei.erasmus.pp.eventmate.ui.submission

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.tei.erasmus.pp.eventmate.app.App
import gr.tei.erasmus.pp.eventmate.data.model.SubmissionFile
import gr.tei.erasmus.pp.eventmate.data.model.SubmissionResponse
import gr.tei.erasmus.pp.eventmate.helpers.FileHelper
import gr.tei.erasmus.pp.eventmate.ui.base.*
import kotlinx.coroutines.launch
import timber.log.Timber

class SubmissionViewModel : BaseViewModel() {
	private val submissionRepository by lazy { App.COMPONENTS.provideSubmissionRepository() }
	
	private val mStates = MutableLiveData<State>()
	val states: LiveData<State>
		get() = mStates
	
	fun getUserTaskSubmissions(userId: Long, taskId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = submissionRepository.getSubmissions(userId, taskId).await()
				if (response.isSuccessful && response.body() != null) {
					mStates.postValue(SubmissionState(response.body()!!))
				}
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun saveNewSubmissionFile(taskId: Long, submissionFile: SubmissionFile) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = submissionRepository.saveNewSubmissionFile(taskId, submissionFile).await()
				Timber.d("saveNewSubmissionFile() $response ${response.isSuccessful}")
				if (response.isSuccessful && response.body() != null) {
					mStates.postValue(FinishedState)
				}
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	fun saveFileLocally(context: Context, submissionFile: SubmissionFile) {
		
		launch {
			mStates.postValue(LoadingState)
			when (submissionFile.type) {
				SubmissionFile.FileType.PHOTO.name -> submissionFile.name?.let {
					FileHelper.saveFileLocally(
						context,
						submissionFile.data,
						"${submissionFile.name}.jpeg",
						"image/jpeg"
					)
				}
				SubmissionFile.FileType.VIDEO.name -> FileHelper.saveFileLocally(
					context,
					submissionFile.data,
					"${submissionFile.name}.mp4",
					"video/mp4"
				)
				
				SubmissionFile.FileType.AUDIO.name -> FileHelper.saveFileLocally(
					context,
					submissionFile.data,
					"${submissionFile.name}.wav",
					"audio/wav"
				)
			}
			
			mStates.postValue(FinishedState)
		}
		
	}
	
	fun deleteSubmissionFile(submissionFileId: Long) {
		launch {
			mStates.postValue(LoadingState)
			try {
				val response = submissionRepository.deleteSubmissionFile(submissionFileId).await()
				Timber.d("deleteSubmissionFile() $response ${response.isSuccessful}")
				val state = if (response.isSuccessful && response.body() != null) {
					SubmissionState(response.body()!!)
				} else {
					ErrorState(Throwable("Error"))
				}
				mStates.postValue(state)
			} catch (error: Throwable) {
				mStates.postValue(ErrorState(error))
			}
		}
	}
	
	data class SubmissionState(val submissionResponses: MutableList<SubmissionResponse>) : State() {
		companion object {
			fun from(list: MutableList<SubmissionResponse>): SubmissionState {
				return if (list.isEmpty()) error("submission list should not be empty")
				else {
					SubmissionState(list)
				}
			}
		}
	}
}