package gr.tei.erasmus.pp.eventmate.annotations

import com.mobsandgeeks.saripaar.AnnotationRule
import gr.tei.erasmus.pp.eventmate.helpers.DateTimeHelper


class FutureDateRule(ruleAnnotation: FutureDate?) : AnnotationRule<FutureDate, String>(ruleAnnotation) {
	override fun isValid(dateString: String?): Boolean {
		val parsedDate = DateTimeHelper.parseDateTimeFromString(dateString, DateTimeHelper.DATE_FORMAT)
		return parsedDate != null && (parsedDate.isAfterNow || DateTimeHelper.isToday(parsedDate))
	}
}
