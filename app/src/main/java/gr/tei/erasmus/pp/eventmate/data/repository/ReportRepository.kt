package gr.tei.erasmus.pp.eventmate.data.repository

import android.content.Context
import gr.tei.erasmus.pp.eventmate.data.model.Report
import gr.tei.erasmus.pp.eventmate.helpers.RestHelper

class ReportRepository(private val context: Context, private val restHelper: RestHelper) {
	fun getReports(eventId: Long) = restHelper.getEventReports(eventId)
	
	fun getReport(reportId: Long) = restHelper.getEventReport(reportId)
	
	fun saveReport(eventId: Long, report: Report) = restHelper.saveEventReport(eventId, report)
	
	fun deleteReport(reportId: Long) = restHelper.deleteEventReport(reportId)
	
}