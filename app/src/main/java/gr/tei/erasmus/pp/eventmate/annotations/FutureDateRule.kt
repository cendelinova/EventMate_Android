package gr.tei.erasmus.pp.eventmate.annotations

import com.mobsandgeeks.saripaar.AnnotationRule
import gr.tei.erasmus.pp.eventmate.helpers.DateTimeHelper
import timber.log.Timber
import java.text.ParseException
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat



class FutureDateRule(ruleAnnotation: FutureDate?) : AnnotationRule<FutureDate, String>(ruleAnnotation) {
	override fun isValid(dateString: String?): Boolean {
		val formatter = DateTimeFormat.forPattern(DateTimeHelper.DATE_FORMAT)
		var parsedDate: DateTime? = null

		if (dateString.isNullOrEmpty()) return false
		
		try {
			parsedDate = formatter.parseDateTime(dateString)
		} catch (ignored: ParseException) {
			Timber.e(ignored)
		}
		
		return parsedDate != null && (parsedDate.isAfterNow || DateTimeHelper.isToday(parsedDate))
	}
}
