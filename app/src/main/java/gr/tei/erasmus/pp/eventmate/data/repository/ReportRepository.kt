package gr.tei.erasmus.pp.eventmate.data.repository

import android.content.Context
import gr.tei.erasmus.pp.eventmate.data.model.Email
import gr.tei.erasmus.pp.eventmate.data.model.ReportRequest
import gr.tei.erasmus.pp.eventmate.helpers.RestHelper

class ReportRepository(private val context: Context, private val restHelper: RestHelper) {
	fun getReports(eventId: Long) = restHelper.getEventReports(eventId)
	
	fun getReport(reportId: Long) = restHelper.getEventReport(reportId)
	
	fun saveReport(eventId: Long, report: ReportRequest) = restHelper.saveEventReport(eventId, report)
	
	fun deleteReport(reportId: Long) = restHelper.deleteEventReport(reportId)
	fun shareReport(reportId: Long, email: Email) = restHelper.shareReport(reportId, email)
	
	fun downloadReport(reportId: Long) = restHelper.downloadReport(reportId)
	
}