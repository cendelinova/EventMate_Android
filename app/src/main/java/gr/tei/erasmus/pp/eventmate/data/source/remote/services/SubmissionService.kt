package gr.tei.erasmus.pp.eventmate.data.source.remote.services

import gr.tei.erasmus.pp.eventmate.data.model.SubmissionResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SubmissionService {
	@GET("/task/{taskId}/submission/{userId}")
	fun getSubmissions(@Path("taskId") taskId: Long, @Path("userId") userId: Long): Deferred<Response<MutableList<SubmissionResponse>>>
}