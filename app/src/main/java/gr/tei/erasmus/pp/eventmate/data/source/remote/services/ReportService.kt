package gr.tei.erasmus.pp.eventmate.data.source.remote.services

import gr.tei.erasmus.pp.eventmate.data.model.Report
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface ReportService {
	@GET("/event/{id}/reports")
	fun getEventReports(@Path("id") id: Long): Deferred<Response<MutableList<Report>>>
	
	@GET("/report/{id}")
	fun getReport(@Path("id") id: Long): Deferred<Response<Report>>
	
	@DELETE("/report/{id}")
	fun deleteReport(@Path("id") id: Long): Deferred<Response<MutableList<Report>>>
	
	@POST("/event/{id}/report")
	fun saveReport(@Path("id") id: Long, @Body report: Report): Deferred<Response<Report>>
}