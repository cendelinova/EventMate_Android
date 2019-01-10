package gr.tei.erasmus.pp.eventmate.data.model

import gr.tei.erasmus.pp.eventmate.R
import java.util.*

data class Report(
	val id: Long?, val name: String?, val type: String, val comment: String?, val preview: String?, val content: String?) {
	
	enum class ReportType(val icon: Int, var defaultString: Int) {
		CERTIFICATE(R.drawable.ic_certificate_white_20dp, R.string.certificate),
		FULL_SUMMARY(R.drawable.ic_full_report_white_20dp, R.string.full_report)
	}
	
}
