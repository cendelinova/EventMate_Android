package gr.tei.erasmus.pp.eventmate.data.repository

import android.content.Context
import gr.tei.erasmus.pp.eventmate.data.model.SubmissionFile
import gr.tei.erasmus.pp.eventmate.helpers.RestHelper

class SubmissionRepository(private val context: Context, private val restHelper: RestHelper) {
	suspend fun getSubmissions(userId: Long, taskId: Long) = restHelper.getUserTaskSubmissions(userId, taskId)
	
	suspend fun saveNewSubmissionFile(taskId: Long, submissionFile: SubmissionFile) = restHelper.saveSubmission(taskId, submissionFile)
}