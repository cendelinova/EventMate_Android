package gr.tei.erasmus.pp.eventmate.annotations

import com.mobsandgeeks.saripaar.AnnotationRule
import gr.tei.erasmus.pp.eventmate.helpers.DateTimeHelper
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FutureDateRule(ruleAnnotation: FutureDate?) : AnnotationRule<FutureDate, String>(ruleAnnotation) {
	override fun isValid(dateString: String?): Boolean {
		val sdf = SimpleDateFormat(DateTimeHelper.TIME_FORMAT, Locale.US)
		var parsedDate: Date? = null
		try {
			parsedDate = sdf.parse(dateString)
		} catch (ignored: ParseException) {
			Timber.e(ignored)
		}
		
		return parsedDate != null && parsedDate.after(Date())
	}
	
	
}
