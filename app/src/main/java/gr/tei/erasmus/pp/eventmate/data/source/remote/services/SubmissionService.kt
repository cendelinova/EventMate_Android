package gr.tei.erasmus.pp.eventmate.data.source.remote.services

import gr.tei.erasmus.pp.eventmate.data.model.SubmissionFile
import gr.tei.erasmus.pp.eventmate.data.model.SubmissionRequest
import gr.tei.erasmus.pp.eventmate.data.model.SubmissionResponse
import gr.tei.erasmus.pp.eventmate.data.model.Task
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SubmissionService {
	@GET("/task/{taskId}/submission/{userId}")
	fun getSubmissions(@Path("taskId") taskId: Long, @Path("userId") userId: Long): Deferred<Response<MutableList<SubmissionResponse>>>
	
	@POST("/task/{taskId}/submission")
	fun createSubmission(@Path("taskId") taskId: Long, @Body submissionRequest: SubmissionRequest): Deferred<Response<Task>>
	
	@POST("/file/submissionFile/task/{taskId}")
	fun saveSubmissionFile(@Path("taskId") taskId: Long, @Body submissionFile: SubmissionFile): Deferred<Response<Task>>
}