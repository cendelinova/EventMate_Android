package gr.tei.erasmus.pp.eventmate.data.source.remote.services

import gr.tei.erasmus.pp.eventmate.data.model.Email
import gr.tei.erasmus.pp.eventmate.data.model.ReportRequest
import gr.tei.erasmus.pp.eventmate.data.model.ReportResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface ReportService {
	@GET("/event/{id}/reports")
	fun getEventReports(@Path("id") id: Long): Deferred<Response<MutableList<ReportResponse>>>
	
	@GET("/report/{id}")
	fun getReport(@Path("id") id: Long): Deferred<Response<ReportResponse>>
	
	@DELETE("/report/{id}")
	fun deleteReport(@Path("id") id: Long): Deferred<Response<MutableList<ReportResponse>>>
	
	@POST("/event/{id}/report")
	fun saveReport(@Path("id") id: Long, @Body report: ReportRequest): Deferred<Response<ReportResponse>>
	
	@POST("/report/{id}/share")
	fun shareReport(@Path("id") id: Long, @Body email: Email): Deferred<Response<Unit>>
}