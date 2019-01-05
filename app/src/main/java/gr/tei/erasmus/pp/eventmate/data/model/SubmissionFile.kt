package gr.tei.erasmus.pp.eventmate.data.model

import gr.tei.erasmus.pp.eventmate.R

data class SubmissionFile(val name: String, val type: String) {
	
	
	
	enum class FileType(var icon: Int) {
		PHOTO(R.drawable.ic_photo),
		VIDEO(R.drawable.ic_video),
		AUDIO(R.drawable.ic_audio)
	}
	
}

