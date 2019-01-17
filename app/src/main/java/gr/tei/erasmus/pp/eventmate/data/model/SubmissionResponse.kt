package gr.tei.erasmus.pp.eventmate.data.model

data class SubmissionResponse(
	val id: Long,
	val taskName: String,
	val taskPhoto: String?,
	val taskDescription: String?,
	val content: List<SubmissionFile>,
	val submitter: User,
	val parentTaskId: Long,
	val earnedPoints: Long) {
	
}
