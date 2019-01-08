package gr.tei.erasmus.pp.eventmate.data.model

import gr.tei.erasmus.pp.eventmate.R

data class SubmissionFile(val id: Long, val name: String?, val comment: String?, val type: String) {
	
	
	enum class FileType(var icon: Int, var defaultString: Int) {
		PHOTO(R.drawable.ic_photo, R.string.photo_proof),
		VIDEO(R.drawable.ic_video, R.string.video_proof),
		AUDIO(R.drawable.ic_audio, R.string.audio_proof)
	}
	
}

