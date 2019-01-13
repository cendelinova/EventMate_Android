package gr.tei.erasmus.pp.eventmate.data.model

data class ReportInfoDTO (
	var includeName: Boolean = true,
	var includePlace: Boolean = true,
	var includeOwner: Boolean = false,
	var includeDate: Boolean = true,
	var includeReportCreator: Boolean = false,
	var includeReportCreatedDate: Boolean = false,
	var listOfIncludedGuests: MutableList<Long> = mutableListOf(),
	var listOfIncludedTasks: MutableList<Long> = mutableListOf()
)