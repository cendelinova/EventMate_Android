package gr.tei.erasmus.pp.eventmate.data.model

data class ReportRequest(
	val name: String?,
	val comment: String?,
	val type: String,
	val reportInfoDTO: ReportInfoDTO
)